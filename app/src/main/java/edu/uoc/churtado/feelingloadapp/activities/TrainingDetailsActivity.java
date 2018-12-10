package edu.uoc.churtado.feelingloadapp.activities;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import java.text.SimpleDateFormat;
import java.util.Locale;
import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.adapters.PlayerRPEAdapter;
import edu.uoc.churtado.feelingloadapp.models.Coach;
import edu.uoc.churtado.feelingloadapp.models.Training;

public class TrainingDetailsActivity extends AppCompatActivity {
    public static final String ARG_ITEM_ID = "item_id";
    private int trainingPosition;
    private Coach coach;
    private Training currentTraining;
    private TextView trainingDate, maxRpe, minRpe, avgRpe;
    private GraphView graph;
    private View recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_details);

        trainingDate = findViewById(R.id.training_date);
        maxRpe = findViewById(R.id.rpe_max);
        minRpe = findViewById(R.id.rpe_min);
        avgRpe = findViewById(R.id.rpe_avg);
        graph = findViewById(R.id.rpe_summary);
        recyclerView = findViewById(R.id.players_list);

        trainingPosition = getIntent().getIntExtra(ARG_ITEM_ID, 0);
        fillUi();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        // Setup linear layout manager to the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //Set adapter with training RPEs
        recyclerView.setAdapter(new PlayerRPEAdapter(currentTraining.getRPEs()));
        //Set divider between list elements
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));
    }

    private void fillUi(){
        //Get current user and read database
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String email = currentUser.getEmail().replaceAll("[@.]","");;
        DatabaseReference reference = database.getReference("/users/" + email);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                coach = dataSnapshot.getValue(Coach.class);
                fillTrainingInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fillTrainingInfo(){
        //Get current training
        currentTraining = coach.getTrainings().get(trainingPosition);
        //Set training date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        trainingDate.setText(dateFormat.format(currentTraining.getDate()));
        //Get and set training rpe info
        maxRpe.setText(getString(R.string.max) + getMaxRpe());
        minRpe.setText(getString(R.string.min) + getMinRpe());
        avgRpe.setText(getString(R.string.avg) + getAvgRpe());

        //Draw bar graph with rpe information
        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, getRpeCount(0)),
                new DataPoint(1, getRpeCount(1)),
                new DataPoint(2, getRpeCount(2)),
                new DataPoint(3, getRpeCount(3)),
                new DataPoint(4, getRpeCount(4)),
                new DataPoint(5, getRpeCount(5)),
                new DataPoint(6, getRpeCount(6)),
                new DataPoint(7, getRpeCount(7)),
                new DataPoint(8, getRpeCount(8)),
                new DataPoint(9, getRpeCount(9)),
                new DataPoint(10, getRpeCount(10))
        });
        graph.addSeries(series);

        // set bar colors
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        // set spacing between bars
        series.setSpacing(20);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);

        //Set manual bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        setupRecyclerView((RecyclerView) recyclerView);
    }

    private int getRpeCount(int rpe){
        //Get number of registered rpes with given value
        int count = 0;
        for(int i = 0; i < currentTraining.getRPEs().size(); ++i){
            if(currentTraining.getRPEs().get(i).getRPE() == rpe){
                ++count;
            }
        }
        return count;
    }

    private int getMinRpe(){
        //Get min value of registered rpes
        int minRpe = Integer.MAX_VALUE;
        for (int i = 0; i < currentTraining.getRPEs().size(); i++) {
            int rpe = currentTraining.getRPEs().get(i).getRPE();
            if (rpe < minRpe) {
                minRpe = rpe;
            }
        }
        return minRpe;
    }

    private int getMaxRpe(){
        //Get max value of registered rpes
        int maxRpe = Integer.MIN_VALUE;
        for (int i = 0; i < currentTraining.getRPEs().size(); i++) {
            int rpe = currentTraining.getRPEs().get(i).getRPE();
            if (rpe > maxRpe) {
                maxRpe = rpe;
            }
        }
        return maxRpe;
    }

    private float getAvgRpe(){
        //Get avg value from registered rpes
        int rpeSum = 0;
        for(int i = 0; i < currentTraining.getRPEs().size(); ++i){
            rpeSum += currentTraining.getRPEs().get(i).getRPE();
        }
        return rpeSum/currentTraining.getRPEs().size();
    }
}

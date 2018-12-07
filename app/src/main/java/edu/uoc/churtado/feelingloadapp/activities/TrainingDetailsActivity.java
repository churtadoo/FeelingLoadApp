package edu.uoc.churtado.feelingloadapp.activities;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Locale;

import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.models.Coach;
import edu.uoc.churtado.feelingloadapp.models.Player;
import edu.uoc.churtado.feelingloadapp.models.Training;

public class TrainingDetailsActivity extends AppCompatActivity {
    public static final String ARG_ITEM_ID = "item_id";
    private int trainingPosition;
    private Coach coach;
    private Training currentTraining;
    private TextView trainingDate, maxRpe, minRpe, avgRpe;
    private GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_details);

        trainingDate = findViewById(R.id.training_date);
        maxRpe = findViewById(R.id.rpe_max);
        minRpe = findViewById(R.id.rpe_min);
        avgRpe = findViewById(R.id.rpe_avg);
        graph = findViewById(R.id.rpe_summary);

        trainingPosition = getIntent().getIntExtra(ARG_ITEM_ID, 0);
        fillUi();
    }

    private void fillUi(){
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
        currentTraining = coach.getTrainings().get(trainingPosition);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
        trainingDate.setText(dateFormat.format(currentTraining.getDate()));
        maxRpe.setText("Max: " + getMaxRpe());
        minRpe.setText("Min: " + getMinRpe());
        avgRpe.setText("Avg: " + getAvgRpe());
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

        // styling
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        series.setSpacing(20);

        // draw values on top
        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.BLACK);
        //series.setValuesOnTopSize(50);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
    }

    private int getRpeCount(int rpe){
        int count = 0;
        for(int i = 0; i < currentTraining.getRPEs().size(); ++i){
            if(currentTraining.getRPEs().get(i).getRPE() == rpe){
                ++count;
            }
        }
        return count;
    }

    private int getMinRpe(){
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
        int rpeSum = 0;
        for(int i = 0; i < currentTraining.getRPEs().size(); ++i){
            rpeSum += currentTraining.getRPEs().get(i).getRPE();
        }
        return rpeSum/currentTraining.getRPEs().size();
    }
}

package edu.uoc.churtado.feelingloadapp.activities;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.models.Coach;
import edu.uoc.churtado.feelingloadapp.models.PlayerRPE;
import edu.uoc.churtado.feelingloadapp.models.Training;

public class RPESummaryActivity extends AppCompatActivity implements View.OnClickListener{
    private Coach coach;

    private static final String ZERO = "0";
    private static final String BAR = "/";

    //Calendar to get date and time
    public final Calendar c = Calendar.getInstance();

    //Vars to get date
    final int selectedMonth = c.get(Calendar.MONTH);
    final int selectedDay = c.get(Calendar.DAY_OF_MONTH);
    final int selectedYear = c.get(Calendar.YEAR);

    int startYear, startMonth, startDay, endYear, endMonth, endDay;

    //Widgets
    EditText editTextDateStart, editTextDateEnd;
    ImageButton ibGetDateStart, ibGetDateEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rpesummary);

        //Edit Texts to show selected dates
        editTextDateStart = findViewById(R.id.et_mostrar_fecha_picker);
        editTextDateEnd = findViewById(R.id.et_mostrar_fecha_picker2);
        //Buttons to show date pickers
        ibGetDateStart = findViewById(R.id.ib_obtener_fecha);
        ibGetDateEnd = findViewById(R.id.ib_obtener_fecha2);
        ibGetDateStart.setOnClickListener(this);
        ibGetDateEnd.setOnClickListener(this);

        //Button to update graph with selected dates
        Button updateGraphButton = findViewById(R.id.updateGraph);
        updateGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGraph();
            }
        });

        fillCurrentCoach();
    }

    private void updateGraph(){
        //To update graph info, get all trainings between selected dates
        Calendar calendar = Calendar.getInstance();
        calendar.set(startYear, startMonth, startDay);
        Date startDate = calendar.getTime();
        calendar.set(endYear, endMonth, endDay);
        Date endDate = calendar.getTime();
        int[] trainingsInfo = new int[11];
        for(int i = 0; i < coach.getTrainings().size(); ++i){
            Date trainingDate = coach.getTrainings().get(i).getDate();
            if(trainingDate.after(startDate) && trainingDate.before(endDate)){
                for(int x = 0; x < coach.getTrainings().get(i).getRPEs().size(); ++ x){
                    PlayerRPE playerRpe = coach.getTrainings().get(i).getRPEs().get(x);
                    trainingsInfo[playerRpe.getRPE()]++;
                }
            }
        }

        //Draw graph with sum of registered rpes for each value
        GraphView graph = findViewById(R.id.rpe_summary);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, trainingsInfo[0]),
                new DataPoint(1, trainingsInfo[1]),
                new DataPoint(2, trainingsInfo[2]),
                new DataPoint(3, trainingsInfo[3]),
                new DataPoint(4, trainingsInfo[4]),
                new DataPoint(5, trainingsInfo[5]),
                new DataPoint(6, trainingsInfo[6]),
                new DataPoint(7, trainingsInfo[7]),
                new DataPoint(8, trainingsInfo[8]),
                new DataPoint(9, trainingsInfo[9]),
                new DataPoint(10, trainingsInfo[10])
        });
        graph.addSeries(series);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);

        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_obtener_fecha:
                getDateStart();
                break;
            case R.id.ib_obtener_fecha2:
                getDateEnd();
                break;
        }
    }

    private void getDateStart(){
        DatePickerDialog getStartDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Because month start in 0
                final int currentMonth = month + 1;
                //Format date and show it
                String formattedDay = (dayOfMonth < 10)? ZERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String formattedMonth = (currentMonth < 10)? ZERO + String.valueOf(currentMonth):String.valueOf(currentMonth);
                editTextDateStart.setText(formattedDay + BAR + formattedMonth + BAR + year);
                startDay = dayOfMonth;
                startMonth = month;
                startYear = year;
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },selectedYear, selectedMonth, selectedDay);
        //Show the widget
        getStartDate.show();
    }

    private void getDateEnd(){
        DatePickerDialog getStartDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Because month start in 0
                final int currentMonth = month + 1;
                //Format date and show it
                String formattedDay = (dayOfMonth < 10)? ZERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String formattedMonth = (currentMonth < 10)? ZERO + String.valueOf(currentMonth):String.valueOf(currentMonth);
                editTextDateEnd.setText(formattedDay + BAR + formattedMonth + BAR + year);
                endDay = dayOfMonth;
                endMonth = month;
                endYear = year;
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },selectedYear, selectedMonth, selectedDay);
        //Show the widget
        getStartDate.show();
    }

    private void fillCurrentCoach(){
        //Get current user and read data from database
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String email = currentUser.getEmail().replaceAll("[@.]","");;
        DatabaseReference reference = database.getReference("/users/" + email);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                coach = dataSnapshot.getValue(Coach.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

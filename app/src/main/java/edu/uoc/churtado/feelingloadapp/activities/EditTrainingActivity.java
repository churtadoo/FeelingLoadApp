package edu.uoc.churtado.feelingloadapp.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;
import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.models.Coach;
import edu.uoc.churtado.feelingloadapp.models.Player;
import edu.uoc.churtado.feelingloadapp.models.Training;
import edu.uoc.churtado.feelingloadapp.models.UserType;

public class EditTrainingActivity extends AppCompatActivity implements View.OnClickListener {
    private Coach coach;

    private static final String ZERO = "0";
    private static final String BAR = "/";
    private static final String TWO_POINTS = ":";
    Training currentTraining = new Training();

    //Calendar to get date and time
    public final Calendar c = Calendar.getInstance();

    //Vars to get date
    final int month = c.get(Calendar.MONTH);
    final int day = c.get(Calendar.DAY_OF_MONTH);
    final int year = c.get(Calendar.YEAR);

    //Vars to get time
    final int hour = c.get(Calendar.HOUR_OF_DAY);
    final int minute = c.get(Calendar.MINUTE);

    int selectedDay = -1, selectedMonth = -1, selectedYear = -1, selectedHour = -1, selectedMinute = -1;

    //Widgets
    EditText editTextDate, editTextTime;
    ImageButton ibGetDate, ibGetTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_training);

        //EditText to show selected date
        editTextDate = findViewById(R.id.et_mostrar_fecha_picker);
        //Button to show date picker
        ibGetDate = findViewById(R.id.ib_obtener_fecha);
        ibGetDate.setOnClickListener(this);
        //EditText to show selected time
        editTextTime = findViewById(R.id.et_mostrar_hora_picker);
        //Button to show time picker
        ibGetTime = findViewById(R.id.ib_obtener_hora);
        ibGetTime.setOnClickListener(this);

        Button saveTrainingButton = findViewById(R.id.saveTraining);
        saveTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTraining();
            }
        });

        fillCurrentCoach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_obtener_fecha:
                getDate();
                break;
            case R.id.ib_obtener_hora:
                getTime();
                break;
        }
    }

    private void saveTraining(){
        //Form validations
        if (selectedDay == -1 || selectedMonth == -1 || selectedYear == -1) {
            editTextDate.setError("Please enter a date");
            editTextDate.requestFocus();
            return;
        }
        if (selectedHour == -1 || selectedMinute == -1) {
            editTextTime.setError("Please enter a time");
            editTextTime.requestFocus();
            return;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);
        currentTraining.setDate(calendar.getTime());
        coach.addTraining(calendar.getTime());
        //Update users info with new training
        String userEmail = coach.getEmail().replaceAll("[@.]","");
        FirebaseDatabase.getInstance().getReference().child("users").child(userEmail).setValue(coach)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "success");
                        updatePlayers();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "failure");
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("TAG", "complete");
                    }
                });
    }

    private void updatePlayers(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("users");
        final EditTrainingActivity editTrainingActivity = this;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot user : snapshot.getChildren()) {
                    Player player = user.getValue(Player.class);
                    String playerCoachEmail = player.getCoachEmail();
                    if(player.getType() == UserType.Player && playerCoachEmail != null && playerCoachEmail.equals(coach.getEmail())) {
                        player.addTraining(currentTraining.getDate());
                        user.getRef().setValue(player);
                    }
                }
                Toast.makeText(editTrainingActivity, "Training successfully updated", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), MainCoachActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getTime(){
        TimePickerDialog getTime = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Format time
                String formattedHour =  (hourOfDay < 10)? String.valueOf(ZERO + hourOfDay) : String.valueOf(hourOfDay);
                String formattedMinute = (minute < 10)? String.valueOf(ZERO + minute):String.valueOf(minute);
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                //Show selected time
                editTextTime.setText(formattedHour + TWO_POINTS + formattedMinute + " " + AM_PM);
                selectedHour = hourOfDay;
                selectedMinute = minute;
            }
            //Estos valores deben ir en ese orden
            //Al colocar en false se muestra en formato 12 horas y true en formato 24 horas
            //Pero el sistema devuelve la hora en formato 24 horas
        }, hour, minute, false);

        getTime.show();
    }

    private void getDate(){
        DatePickerDialog getStartDate = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Because month start in 0
                final int currentMonth = month + 1;
                //Format date and show it
                String formattedDay = (dayOfMonth < 10)? ZERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                String formattedMonth = (currentMonth < 10)? ZERO + String.valueOf(currentMonth):String.valueOf(currentMonth);
                editTextDate.setText(formattedDay + BAR + formattedMonth + BAR + year);
                selectedDay = dayOfMonth;
                selectedMonth = month;
                selectedYear = year;
            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },year, month, day);
        //Show the widget
        getStartDate.show();

    }

    private void fillCurrentCoach(){
        //Read user info from database
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

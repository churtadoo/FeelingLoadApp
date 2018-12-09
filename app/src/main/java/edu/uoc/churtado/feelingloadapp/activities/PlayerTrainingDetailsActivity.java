package edu.uoc.churtado.feelingloadapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.models.Coach;
import edu.uoc.churtado.feelingloadapp.models.Player;
import edu.uoc.churtado.feelingloadapp.models.PlayerTraining;
import edu.uoc.churtado.feelingloadapp.models.UserType;

public class PlayerTrainingDetailsActivity extends AppCompatActivity {
    public static final String ARG_ITEM_ID = "item_id";
    private Player player;
    private int playerTrainingPosition;

    private TextView playerTrainingDate, playerTrainingRpe;
    private NumberPicker newPlayerTrainingRpe;
    private Button saveRpe;

    private PlayerTraining playerTraining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        playerTrainingPosition = getIntent().getIntExtra(ARG_ITEM_ID, 0);
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
                player = dataSnapshot.getValue(Player.class);
                fillTrainingInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fillTrainingInfo(){
        List<PlayerTraining> playerTrainings = player.getTrainings();
        playerTraining = playerTrainings.get(playerTrainingPosition);
        if(playerTraining.HasRegisteredRPE()){
            setContentView(R.layout.player_training_details_registered);
            playerTrainingDate = findViewById(R.id.playertraining_date);
            playerTrainingRpe = findViewById(R.id.playertraining_rpe);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
            playerTrainingDate.setText(dateFormat.format(playerTraining.getDate()));
            playerTrainingRpe.setText("RPE: " + playerTraining.getRPE());
        }
        else {
            setContentView(R.layout.player_training_details_new_register);
            playerTrainingDate = findViewById(R.id.playertraining_date);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
            playerTrainingDate.setText(dateFormat.format(playerTraining.getDate()));

            newPlayerTrainingRpe = findViewById(R.id.playertraining_rpe);
            newPlayerTrainingRpe.setMinValue(1);
            newPlayerTrainingRpe.setMaxValue(10);

            saveRpe = findViewById(R.id.save_playertraining_rpe);
            saveRpe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveNewData();
                }
            });
        }
    }

    private void saveNewData(){
        player.registerRpe(playerTrainingPosition, newPlayerTrainingRpe.getValue());
        String userEmail = player.getEmail().replaceAll("[@.]","");
        final PlayerTrainingDetailsActivity playerTrainingDetailsActivity = this;
        FirebaseDatabase.getInstance().getReference().child("users").child(userEmail).setValue(player)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "success");
                        updateCoachInfo();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(playerTrainingDetailsActivity, "Error updating rpe", Toast.LENGTH_LONG).show();
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

    private void updateCoachInfo(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        String coachEmail = player.getCoachEmail().replaceAll("[@.]", "");
        Query query = reference.child("users").child(coachEmail);
        final PlayerTrainingDetailsActivity playerTrainingDetailsActivity = this;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                Coach coach = snapshot.getValue(Coach.class);
                coach.registerRpe(playerTraining.getDate(), newPlayerTrainingRpe.getValue(), player.getEmail());
                snapshot.getRef().setValue(coach);
                Toast.makeText(playerTrainingDetailsActivity, "Successfully updated rpe", Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), MainPlayerActivity.class);
                startActivity(i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
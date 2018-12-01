package edu.uoc.churtado.feelingloadapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.adapters.PlayerTrainingAdapter;
import edu.uoc.churtado.feelingloadapp.models.Player;
import edu.uoc.churtado.feelingloadapp.models.PlayerTraining;
import edu.uoc.churtado.feelingloadapp.models.User;
import edu.uoc.churtado.feelingloadapp.models.UserType;

public class MainPlayerActivity extends AppCompatActivity implements ListView.OnItemClickListener {
    private PlayerTrainingAdapter playerTrainingAdapter;
    private ArrayList<PlayerTraining> playerTrainings = new ArrayList<>();

    private ListView playerTrainingsList;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_player);

        playerTrainingsList = (ListView) findViewById(R.id.playertrainings_list);

        fillCurrentPlayer();
    }

    private void fillPlayerTrainingsListView(){
       playerTrainings = player.getTrainings();
       playerTrainings.add(new PlayerTraining(new Date(2018, 9, 22)));
       playerTrainings.add(new PlayerTraining(new Date(2018, 9, 22)));
       playerTrainings.add(new PlayerTraining(new Date(2018, 9, 22)));
       playerTrainingAdapter = new PlayerTrainingAdapter(this, playerTrainings);
       playerTrainingsList.setAdapter(playerTrainingAdapter);
       //playerTrainingsList.setOnClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        //Create intent with new activity
        Intent intent = new Intent(MainPlayerActivity.this, TrainingDetailsActivity.class);
        //Pass book_id to BookDetailActivity
        intent.putExtra("playertraining_id", 1);
        //Start the new activity
        startActivity(intent);
    }

    private void fillCurrentPlayer(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String email = currentUser.getEmail().replaceAll("[@.]","");;
        DatabaseReference reference = database.getReference("/users/" + email);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                player = dataSnapshot.getValue(Player.class);
                fillPlayerTrainingsListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

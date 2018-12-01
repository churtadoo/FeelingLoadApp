package edu.uoc.churtado.feelingloadapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

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

    private TextView playerName;
    private ImageView playerPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_player);

        playerTrainingsList = (ListView) findViewById(R.id.playertrainings_list);
        playerName = (TextView) findViewById(R.id.player_name);
        playerPhoto = (ImageView) findViewById(R.id.player_photo);

        fillCurrentPlayer();
    }

    private void fillPlayerTrainingsListView(){
       playerTrainings = player.getTrainings();
       playerTrainings.add(new PlayerTraining(new Date()));
       playerTrainings.add(new PlayerTraining(new Date()));
       PlayerTraining test = new PlayerTraining(new Date());
       test.registerRPE(7);
       playerTrainings.add(test);
       playerTrainingAdapter = new PlayerTrainingAdapter(this, playerTrainings);

       LayoutInflater inflater = getLayoutInflater();
       ViewGroup header = (ViewGroup)inflater.inflate(R.layout.playertraining_list_header, playerTrainingsList, false);
       playerTrainingsList.addHeaderView(header, null, false);
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
                playerName.setText(player.getDisplayName());
                //Picasso.get().load(player.getUrlPhoto()).into(playerPhoto);
                fillPlayerTrainingsListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

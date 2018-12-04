package edu.uoc.churtado.feelingloadapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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

import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.adapters.PlayerTrainingAdapter;
import edu.uoc.churtado.feelingloadapp.models.Player;
import edu.uoc.churtado.feelingloadapp.models.PlayerTraining;

public class MainPlayerActivity extends AppCompatActivity {
    private ArrayList<PlayerTraining> playerTrainings = new ArrayList<>();

    private Player player;

    private TextView playerName;
    private ImageView playerPhoto;
    private View recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_player);

        playerName = findViewById(R.id.player_name);
        playerPhoto = findViewById(R.id.player_photo);

        recyclerView = findViewById(R.id.playertrainings_list);
        assert recyclerView != null;

        fillCurrentPlayer();
    }

    private void fillPlayerTrainingsListView(){
       playerTrainings = player.getTrainings();
       setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        // Setup linear layout manager to the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //BookContent.getBooks() will get all the books from realm database
        recyclerView.setAdapter(new PlayerTrainingAdapter(playerTrainings));
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
                Picasso.get().load(player.getUrlPhoto()).into(playerPhoto);
                fillPlayerTrainingsListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

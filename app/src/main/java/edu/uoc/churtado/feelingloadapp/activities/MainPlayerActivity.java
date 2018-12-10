package edu.uoc.churtado.feelingloadapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import de.hdodenhof.circleimageview.CircleImageView;
import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.adapters.PlayerTrainingAdapter;
import edu.uoc.churtado.feelingloadapp.models.Player;
import edu.uoc.churtado.feelingloadapp.models.PlayerTraining;

public class MainPlayerActivity extends AppCompatActivity {
    private ArrayList<PlayerTraining> playerTrainings = new ArrayList<>();

    private Player player;

    private TextView playerName;
    private CircleImageView playerPhoto;
    private View recyclerView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_player);

        playerName = findViewById(R.id.player_name);
        playerPhoto = findViewById(R.id.player_photo);

        ListView listView = findViewById(R.id.menu);
        final String[] options = { "Logout" };

        listView.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, options));
        // Set the list's click listener
        listView.setOnItemClickListener(new DrawerItemClickListener());

        //Init swipe refresh layout and set listener to refresh info from firebase
        mSwipeRefreshLayout = findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        fillCurrentPlayer();
                        mSwipeRefreshLayout.setRefreshing(false);
                        Snackbar snackbar = Snackbar
                                .make(mSwipeRefreshLayout, "Updated info", Snackbar.LENGTH_LONG);

                        snackbar.show();
                    }
                });

        recyclerView = findViewById(R.id.playertrainings_list);
        assert recyclerView != null;

        fillCurrentPlayer();
    }

    @Override
    public void onBackPressed() {
        //If back button pressed, do nothing
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //If menu item clicked, logout
            logout();
        }
    }

    private void logout() {
        //Logout from Firebase
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent    );
    }

    private void fillPlayerTrainingsListView(){
       playerTrainings = player.getTrainings();
       setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        // Setup linear layout manager to the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        //Set adapter with player trainings
        recyclerView.setAdapter(new PlayerTrainingAdapter(playerTrainings));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));
    }

    private void fillCurrentPlayer(){
        //Read info from database
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String email = currentUser.getEmail().replaceAll("[@.]","");;
        DatabaseReference reference = database.getReference("/users/" + email);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                player = dataSnapshot.getValue(Player.class);
                playerName.setText(player.getDisplayName().toUpperCase());
                if(player.getUrlPhoto() != null && !player.getUrlPhoto().isEmpty()){
                    Picasso.get().load(player.getUrlPhoto()).into(playerPhoto);
                }
                fillPlayerTrainingsListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

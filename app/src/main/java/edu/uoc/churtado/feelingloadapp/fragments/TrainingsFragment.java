package edu.uoc.churtado.feelingloadapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.activities.EditTrainingActivity;
import edu.uoc.churtado.feelingloadapp.activities.RPESummaryActivity;
import edu.uoc.churtado.feelingloadapp.adapters.TrainingAdapter;
import edu.uoc.churtado.feelingloadapp.models.Coach;
import edu.uoc.churtado.feelingloadapp.models.Training;

public class TrainingsFragment extends Fragment {
    private Coach coach;
    private ArrayList<Training> trainings;
    private View recyclerView;
    Button statsButton;

    public TrainingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Initialize elements in view and set click listeners
        recyclerView = getView().findViewById(R.id.trainings_list);
        FloatingActionButton addNewTrainingButton = getView().findViewById(R.id.addNewTraining);
        addNewTrainingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), EditTrainingActivity.class);
                startActivity(i);
            }
        });
        statsButton = getView().findViewById(R.id.stats);
        statsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent i = new Intent(getContext(), RPESummaryActivity.class);
                startActivity(i);
            }
        });
        fillCurrentCoach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.trainings_fragment, container, false);
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
                fillTrainingsListView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fillTrainingsListView(){
        assert recyclerView != null;
        trainings = coach.getTrainings();
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        // Setup linear layout manager to the recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //Set adapter with trainings list
        recyclerView.setAdapter(new TrainingAdapter(trainings));
        //Set divider to list elements
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
    }
}

package edu.uoc.churtado.feelingloadapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.fragments.PlayerTrainingDetailFragment;

public class PlayerTrainingDetailsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity using a fragment transaction.

        }
    }
}
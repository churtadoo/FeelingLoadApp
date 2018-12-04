package edu.uoc.churtado.feelingloadapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;

import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.models.Coach;
import edu.uoc.churtado.feelingloadapp.models.Player;

public class PlayerDetailsActivity extends AppCompatActivity {
    public static final String ARG_ITEM_ID = "item_id";
    private int PICK_IMAGE_REQUEST = 1;
    private int playerPosition;
    private Coach coach;
    private EditText playerName, playerSurname, playerEmail;
    private ImageView playerPhoto;
    private Button selectPhoto, savePlayer;
    private Player currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);

        playerName = findViewById(R.id.editTextName);
        playerSurname = findViewById(R.id.editTextSurname);
        playerEmail = findViewById(R.id.editTextEmail);

        selectPhoto = findViewById(R.id.selectPhoto);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        savePlayer = findViewById(R.id.savePlayer);
        savePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewData();
            }
        });

        if(getIntent().hasExtra(ARG_ITEM_ID)){
            playerPosition = getIntent().getIntExtra(ARG_ITEM_ID, -1);
        }

        fillCurrentCoach();
    }

    private void fillCurrentCoach(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String email = currentUser.getEmail().replaceAll("[@.]","");;
        DatabaseReference reference = database.getReference("/users/" + email);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                coach = dataSnapshot.getValue(Coach.class);
                fillPlayerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fillPlayerView(){
        if(playerPosition != -1) {
            currentPlayer = coach.getPlayers().get(playerPosition);
            playerName.setText(currentPlayer.getName());
            playerSurname.setText(currentPlayer.getSurname());
            playerEmail.setText(currentPlayer.getEmail());
            String currentPlayerPhoto = currentPlayer.getUrlPhoto();
            if(currentPlayerPhoto != null && !currentPlayerPhoto.isEmpty()){
                Picasso.get().load(currentPlayerPhoto).into(playerPhoto);
            }
        }
        else {
            currentPlayer = new Player();
        }
    }

    public void pickImage() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                playerPhoto = (ImageView) findViewById(R.id.player_photo);
                playerPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveNewData(){
        //TODO: validacions
        currentPlayer.setName(String.valueOf(playerName.getText()));
        currentPlayer.setSurname(String.valueOf(playerSurname.getText()));
        currentPlayer.setEmail(String.valueOf(playerEmail.getText()));
        currentPlayer.setCoachEmail(coach.getEmail());
        if(playerPosition != -1){
            coach.updatePlayer(playerPosition, currentPlayer);
        }
        else {
            coach.addPlayer(currentPlayer);
        }
        //TODO: guardar photo url
        String userEmail = coach.getEmail().replaceAll("[@.]","");
        FirebaseDatabase.getInstance().getReference().child("users").child(userEmail).setValue(coach)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "success");
                        String userEmail = currentPlayer.getEmail().replaceAll("[@.]","");
                        FirebaseDatabase.getInstance().getReference().child("users").child(userEmail).setValue(currentPlayer)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "success");
                                        Intent i = new Intent(getApplicationContext(), MainCoachActivity.class);
                                        startActivity(i);
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
}

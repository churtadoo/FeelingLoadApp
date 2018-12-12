package edu.uoc.churtado.feelingloadapp.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.Continuation;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.models.Coach;
import edu.uoc.churtado.feelingloadapp.models.Player;

public class PlayerDetailsActivity extends AppCompatActivity {
    public static final String ARG_ITEM_ID = "item_id";
    private int PICK_IMAGE_REQUEST = 1;
    private int playerPosition = -1;
    private Coach coach;
    private EditText playerName, playerSurname, playerEmail;
    private ImageView playerPhoto;
    private Player currentPlayer;
    private StorageReference mStorageRef;

    private Uri uploadedPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_details);

        playerName = findViewById(R.id.editTextName);
        playerSurname = findViewById(R.id.editTextSurname);
        playerEmail = findViewById(R.id.editTextEmail);
        playerPhoto = findViewById(R.id.player_photo);

        FloatingActionButton selectPhoto = findViewById(R.id.selectPhoto);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        Button savePlayer = findViewById(R.id.savePlayer);
        savePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNewData();
            }
        });

        mStorageRef = FirebaseStorage.getInstance().getReference();

        //Check if editing an existing player
        if(getIntent().hasExtra(ARG_ITEM_ID)){
            playerPosition = getIntent().getIntExtra(ARG_ITEM_ID, -1);
        }

        //If editing player, set email not editable
        if(playerPosition != -1){
            playerEmail.setEnabled(false);
        }

        fillCurrentCoach();
    }

    private void fillCurrentCoach(){
        //Get current user info from database
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
        //Check if editing player to set player existing info in view
        if(playerPosition != -1) {
            currentPlayer = coach.getPlayers().get(playerPosition);
            playerName.setText(currentPlayer.getName());
            playerSurname.setText(currentPlayer.getSurname());
            playerEmail.setText(currentPlayer.getEmail());
        }
        else {
            currentPlayer = new Player();
        }
        String currentPlayerPhoto = currentPlayer.getUrlPhoto();
        if(currentPlayerPhoto != null && !currentPlayerPhoto.isEmpty()){
            Picasso.get().load(currentPlayerPhoto).into(playerPhoto);
        }
    }

    public void pickImage() {
        //Start new intent to pick image from gallery
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //When a picture is taken from the gallery, load it in image view
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uploadedPhotoUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uploadedPhotoUri);
                playerPhoto = findViewById(R.id.player_photo);
                playerPhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveNewData(){
        //Form validation
        if (TextUtils.isEmpty(playerName.getText())) {
            playerName.setError("Please enter a name");
            playerName.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(playerSurname.getText())) {
            playerSurname.setError("Please enter a surname");
            playerSurname.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(playerEmail.getText())) {
            playerEmail.setError("Please enter a email");
            playerEmail.requestFocus();
            return;
        }
        if(playerPosition == -1 && coach.hasPlayer(String.valueOf(playerEmail.getText()))){
            playerEmail.setError("Player already exists with this email");
            playerEmail.requestFocus();
            return;
        }
        //Update player info with values in view
        currentPlayer.setName(String.valueOf(playerName.getText()));
        currentPlayer.setSurname(String.valueOf(playerSurname.getText()));
        currentPlayer.setEmail(String.valueOf(playerEmail.getText()));
        currentPlayer.setCoachEmail(coach.getEmail());
        if(playerPosition != -1){
            coach.updatePlayer(playerPosition, currentPlayer);
        }
        else {
            coach.addPlayer(currentPlayer);
            for (int i = 0; i < coach.getTrainings().size(); ++i){
                currentPlayer.addTraining(coach.getTrainings().get(i).getDate());
            }
        }
        //If photo selected, save it in Firebase Storage
        if(uploadedPhotoUri != null) {
            String playerEmail = currentPlayer.getEmail().replaceAll("[@.]","");
            final StorageReference ref = mStorageRef.child("userImages/" + playerEmail + ".jpg");
            final PlayerDetailsActivity playerDetailsActivity = this;
            UploadTask uploadTask = ref.putFile(uploadedPhotoUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        currentPlayer.setUrlPhoto(String.valueOf(downloadUri));
                        saveCurrentData();
                    } else {
                        Toast.makeText(playerDetailsActivity, "Error updating player", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else{
            saveCurrentData();
        }
    }

    private void saveCurrentData(){
        //Update new player infoin database
        String userEmail = coach.getEmail().replaceAll("[@.]","");
        final PlayerDetailsActivity playerDetailsActivity = this;
                FirebaseDatabase.getInstance().getReference().child("users").child(userEmail).setValue(coach)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("TAG", "success");
                        final String userEmail = currentPlayer.getEmail().replaceAll("[@.]","");
                        FirebaseDatabase.getInstance().getReference().child("users").child(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Player player = dataSnapshot.getValue(Player.class);
                                if(player != null){
                                    player.setName(currentPlayer.getName());
                                    player.setSurname(currentPlayer.getSurname());
                                    player.setUrlPhoto(currentPlayer.getUrlPhoto());
                                    player.setCoachEmail(coach.getEmail());
                                    if(player.getTrainings().size() == 0){
                                        player.setTrainings(currentPlayer.getTrainings());
                                    }
                                }
                                else {
                                    player = currentPlayer;
                                }
                                FirebaseDatabase.getInstance().getReference().child("users").child(userEmail).setValue(player)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "success");
                                                Toast.makeText(playerDetailsActivity, "Successfully updated player", Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(getApplicationContext(), MainCoachActivity.class);
                                                startActivity(i);
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(playerDetailsActivity, "Error updating player", Toast.LENGTH_LONG).show();
                        Log.d("TAG", "failure");
                    }
                });
    }
}

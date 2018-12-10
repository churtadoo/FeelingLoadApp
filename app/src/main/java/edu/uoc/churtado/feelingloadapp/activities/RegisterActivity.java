package edu.uoc.churtado.feelingloadapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.models.User;
import edu.uoc.churtado.feelingloadapp.models.UserType;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextName, editTextSurname, editTextEmail, editTextPassword;
    RadioGroup radioGroupUserType;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        checkLoggedUser();

        editTextName = findViewById(R.id.editTextName);
        editTextSurname = findViewById(R.id.editTextSurname);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        radioGroupUserType = findViewById(R.id.radioUserType);
        progressBar = findViewById(R.id.progressBar);

        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //register user on click
                progressBar.setVisibility(View.VISIBLE);
                registerUser();
            }
        });

        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change to login activity
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        //If back button pressed, do nothing
    }

    private void checkLoggedUser(){
        //if the user is already logged in, show next activity
        if (IsUserSignedIn()) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if(currentUser == null) return;

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String email = currentUser.getEmail().replaceAll("[@.]","");
            DatabaseReference reference = database.getReference("/users/" + email);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    UserType userType = user.getType();
                    goToNextActivity(userType);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void goToNextActivity(UserType userType){
        progressBar.setVisibility(View.GONE);
        //Show different activity according to user's type
        if(userType.equals(UserType.Coach)){
            startActivity(new Intent(RegisterActivity.this, MainCoachActivity.class));
        }
        else {
            startActivity(new Intent(RegisterActivity.this, MainPlayerActivity.class));
        }
    }

    private boolean IsUserSignedIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    private void registerUser(){
        final String name = editTextName.getText().toString().trim();
        final String surname = editTextSurname.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        final String userType = ((RadioButton) findViewById(radioGroupUserType.getCheckedRadioButtonId())).getText().toString();

        //Validate entered data and show errors if needed

        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Please enter name");
            editTextName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(surname)) {
            editTextSurname.setError("Please enter surname");
            editTextSurname.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Please enter your email");
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email");
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter a password");
            editTextPassword.requestFocus();
            return;
        }

        final RegisterActivity registerActivity = this;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, create or update user information from database
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            final String userEmail = firebaseUser.getEmail().replaceAll("[@.]","");
                            if(getUserType(userType) == UserType.Player) {
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/users/" + userEmail);
                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        //If player already exists in database, just check and go to main activity
                                        if(dataSnapshot.getValue() != null){
                                            goToNextActivity(UserType.Player);
                                        }
                                        //If no player exists, create a new one
                                        else {
                                            User user = new User(email, getUserType(userType), name, surname);
                                            FirebaseDatabase.getInstance().getReference().child("users").child(userEmail).setValue(user)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("TAG", "success");
                                                            goToNextActivity(UserType.Player);
                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            //If coach, always create a new user
                            else {
                                User user = new User(email, getUserType(userType), name, surname);
                                FirebaseDatabase.getInstance().getReference().child("users").child(userEmail).setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "success");
                                                goToNextActivity(UserType.Coach);
                                            }
                                        });
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(registerActivity, "Error in register, please try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private UserType getUserType(String userType) {
        if(userType.equals("Coach")) return UserType.Coach;
        else return UserType.Player;
    }
}

package edu.uoc.churtado.feelingloadapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
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

public class LoginActivity extends AppCompatActivity {

    EditText editTextEmail, editTextPassword;

    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressBar);

        checkLoggedUser();

        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Login user
                loginUser();
            }
        });

        findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Go to register activity
                finish();
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        //If button back pressed, do nothing
    }

    private void checkLoggedUser(){
        //If user logged, go to next activity
        if (IsUserSignedIn()) {
            progressBar.setVisibility(View.VISIBLE);
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if(currentUser == null) return;

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            String email = currentUser.getEmail().replaceAll("[@.]","");;
            DatabaseReference reference = database.getReference("/users/" + email);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    UserType userType = user.getType();
                    progressBar.setVisibility(View.GONE);
                    if(userType.equals(UserType.Coach)){
                        startActivity(new Intent(LoginActivity.this, MainCoachActivity.class));
                    }
                    else {
                        startActivity(new Intent(LoginActivity.this, MainPlayerActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private boolean IsUserSignedIn(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    private void loginUser(){
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        //Form validations and how errors

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
        progressBar.setVisibility(View.VISIBLE);
        final LoginActivity loginActivity = this;
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, go to next activity
                            checkLoggedUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(loginActivity, "Error in login, please try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

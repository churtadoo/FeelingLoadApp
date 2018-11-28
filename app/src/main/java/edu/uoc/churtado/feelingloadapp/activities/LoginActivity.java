package edu.uoc.churtado.feelingloadapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import edu.uoc.churtado.feelingloadapp.R;
import edu.uoc.churtado.feelingloadapp.models.UserType;
import edu.uoc.churtado.feelingloadapp.services.UserAuthenticationService;

public class LoginActivity extends AppCompatActivity {

    EditText editTextName, editTextSurname, editTextEmail, editTextPassword;
    RadioGroup radioGroupUserType;

    UserAuthenticationService userAuthenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userAuthenticationService = new UserAuthenticationService();

        //if the user is already logged in, show next activity
        if (userAuthenticationService.IsUserSignedIn()) {
            UserType userType = userAuthenticationService.GetCurrentUserType();
            finish();
            if(userType.equals(UserType.Coach)){
                startActivity(new Intent(this, MainCoachActivity.class));
            }
            else {
                startActivity(new Intent(this, MainPlayerActivity.class));
            }
        }
    }
}

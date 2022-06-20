package com.example.groceriesmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText etLoginUsername;
    private EditText etLoginPassword;
    private TextView tvSignUp;

    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // check if user has already logged in
        if (ParseUser.getCurrentUser() != null){
            // if user is logged in, go directly to main page
            goToMainActivity();
            finish();
        }

        btnLogin = findViewById(R.id.btnLogin);
        etLoginUsername = findViewById(R.id.etLoginUsername);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        tvSignUp = findViewById(R.id.tvSignUp);

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // what will happen when user clicks the login button
                String username = etLoginUsername.getText().toString();
                String password = etLoginPassword.getText().toString();
                ParseUser.logInInBackground(username, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (e != null){ // if there is an error loggin in
                            Log.e(TAG, "issue with login");
                            Toast.makeText(LoginActivity.this, "wrong username or password", Toast.LENGTH_SHORT).show();
                            etLoginUsername.setText(null); // clear username textbox
                            etLoginPassword.setText(null); // clear password textbox
                        }
                        else
                        {
                            Log.d(TAG, "login successful");
                            goToMainActivity();
                        }
                    }
                });

            }
        });
    }


    private void goToMainActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        // this is necessary to prevent user from being able to go back to login page by repeatedly clicking back button
        finish();
    }
}
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

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;




public class RegisterActivity extends AppCompatActivity {

    private TextView tvLogin;
    private EditText etRegisterUsername;
    private EditText etRegisterPassword;
    private Button btnRegister;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tvLogin = findViewById(R.id.tvLogin);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        btnRegister = findViewById(R.id.btnRegister);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // what will happen when the user clicks the sign up button
                String username = etRegisterUsername.getText().toString();
                String password = etRegisterPassword.getText().toString();

                // check if username is valid i.e. no spaces
                if (username.contains(" ")){
                    Toast.makeText(RegisterActivity.this, "username cannot have spaces", Toast.LENGTH_LONG).show();
                }
                else if (username.length() == 0){
                    Toast.makeText(RegisterActivity.this, "please choose a username", Toast.LENGTH_LONG).show();
                }
                // todo: check if no one else has this username
                // check if there is a password
                else {
                    // todo: create new user
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(username);
                    newUser.setPassword(password);
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e!= null){
                                Toast.makeText(RegisterActivity.this, "error encountered signing up", Toast.LENGTH_LONG).show();
                                Log.e(TAG, "error encountered signing up new user: " + e.toString());
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Welcome to Instagram!", Toast.LENGTH_LONG).show();
                                goToMainActivity();
                            }
                        }
                    });
                }

            }
        });
    }

    private void goToMainActivity(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        // line below ensures user cannot go back to login page by repeatedly pressing back
        finish();
    }

}
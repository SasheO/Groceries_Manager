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

import com.example.groceriesmanager.Models.UserList;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
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
                // todo: check if there is a password
                else {
                    // \create new user
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
                                // todo: change the text toasted below when you decide a better name for user
                                Toast.makeText(RegisterActivity.this, "Welcome to Groceries Manager!", Toast.LENGTH_LONG).show();
                                createUserLists();
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
        // line below ensures user cannot go back to register page by repeatedly pressing back
        finish();
    }

    private void createUserLists(){
        UserList groceryList = new UserList();
        groceryList.setUser(ParseUser.getCurrentUser());
        groceryList.setType("grocery");
        groceryList.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "error saving user grocery list in server");
                }
                else{
                    Log.i(TAG, "user grocery list successfully created and saved in server");
                }
            }
        });

        UserList pantryList = new UserList();
        pantryList.setUser(ParseUser.getCurrentUser());
        pantryList.setType("pantry");
        pantryList.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "error saving new user pantry list in server");
                }
                else{
                    Log.i(TAG, "new user pantry list successfully created and saved in server");
                }
            }
        });
    }
}
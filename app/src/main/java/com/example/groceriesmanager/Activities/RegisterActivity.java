package com.example.groceriesmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groceriesmanager.Models.User;
import com.example.groceriesmanager.Models.UserList;
import com.example.groceriesmanager.R;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;



public class RegisterActivity extends AppCompatActivity {

    private TextView tvLogin;
    private EditText etRegisterUsername;
    private EditText etRegisterPassword;
    private Button btnRegister;
    private static final String TAG = "RegisterActivity";
    private String username;
    private String password;

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
                username = etRegisterUsername.getText().toString();
                password = etRegisterPassword.getText().toString();

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
                    // create grocery and pantry list for the new user
                    UserList groceryList = new UserList();
                    groceryList.setType("grocery");
                    UserList pantryList = new UserList();
                    pantryList.setType("pantry");
                    groceryList.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e!=null){
                                Log.e(TAG, "error saving user grocery list in server" + e.toString());
                            }
                            else{
                                Log.i(TAG, "user grocery list successfully created and saved in server");

                                pantryList.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e!=null){
                                            Log.e(TAG, "error saving new user pantry list in server"+ e.toString());
                                        }
                                        else{
                                            Log.i(TAG, "new user pantry list successfully created and saved in server");
                                            createNewUser(username, password, groceryList, pantryList);
                                        }
                                    }
                                });
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

    private void createNewUser(String username, String password, UserList groceryList, UserList pantryList){
        // create new user
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        // set their grocery and pantry list
        newUser.setGroceryList(groceryList);
        newUser.setPantryList(pantryList);

        // sign them up in server
        newUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e!= null){
                    Toast.makeText(RegisterActivity.this, "error encountered signing up", Toast.LENGTH_LONG).show();
                    Log.e(TAG, "error encountered signing up new user: " + e.toString());
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Welcome to " + R.string.app_name, Toast.LENGTH_LONG).show();
                    goToMainActivity();
                }
            }
        });
    }
}
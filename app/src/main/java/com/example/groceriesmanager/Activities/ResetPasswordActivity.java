package com.example.groceriesmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.groceriesmanager.Models.FoodItem;
import com.example.groceriesmanager.Models.User;
import com.example.groceriesmanager.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.util.List;

public class ResetPasswordActivity extends AppCompatActivity {
    private static final  String TAG = "ResetPasswordActivity";
    private Button btnSubmit;
    private EditText etResetPasswordEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        btnSubmit = findViewById(R.id.btnSubmit);
        etResetPasswordEmail = findViewById(R.id.etResetPasswordEmail);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etResetPasswordEmail.getText().toString();
                if (!RegisterActivity.isValidEmail(email)){
                    Toast.makeText(ResetPasswordActivity.this, "type in valid email address", Toast.LENGTH_LONG).show();
                }
                else{
                    // check if email address exists before sending link
                    ParseQuery<User> query = ParseQuery.getQuery(User.class);
                    query.whereEqualTo("email", email);
                    query.findInBackground(new FindCallback<User>() {
                        @Override
                        public void done(List<User> objects, ParseException e) {
                            if (e!=null){
                                Log.e(TAG, "error querying user class: " + e.toString());
                            }
                            else{
                                if (objects.size()!=0){ // if there is a User match with this email address
                                    ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e!=null){
                                                Toast.makeText(ResetPasswordActivity.this, "could not send reset link to given email", Toast.LENGTH_LONG).show();
                                                Log.e(TAG, "reset password error: " + e.toString());
                                            }
                                            else{
                                                Toast.makeText(ResetPasswordActivity.this, "check the link send to your email!", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        }
                                    });
                                }
                                else{
                                    Toast.makeText(ResetPasswordActivity.this, "no such email found", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
                }
            }
        });

    }

}
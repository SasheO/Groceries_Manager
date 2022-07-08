package com.example.groceriesmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.groceriesmanager.Models.User;
import com.example.groceriesmanager.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class AccountSettingsActivity extends AppCompatActivity {
    private CheckBox checkboxVegan;
    private CheckBox checkboxVegetarian;
    private CheckBox checkboxGlutenFree;
    private Button btnSave;
    private Button btnCancel;
    private static final String TAG = "AccountSettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        checkboxGlutenFree = findViewById(R.id.checkboxGlutenFree);
        checkboxVegan = findViewById(R.id.checkboxVegan);
        checkboxVegetarian = findViewById(R.id.checkboxVegetarian);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);

        User currentUser = (User) ParseUser.getCurrentUser();

        // if current user specified any of the following as a diet filter, set the checkbox upon opening the page
        if (currentUser.getDietFilters().contains(getResources().getString(R.string.vegan))){
            checkboxVegan.setChecked(true);
        }
        if (currentUser.getDietFilters().contains(getResources().getString(R.string.vegetarian))){
            checkboxVegetarian.setChecked(true);
        }
        if (currentUser.getDietFilters().contains(getResources().getString(R.string.gluten_free))){
            checkboxGlutenFree.setChecked(true);
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> dietFilters = new ArrayList<>();

                if (checkboxGlutenFree.isChecked()){
                    dietFilters.add(getResources().getString(R.string.gluten_free));
                }
                if (checkboxVegetarian.isChecked()){
                    dietFilters.add(getResources().getString(R.string.vegetarian));
                }
                if (checkboxVegan.isChecked()){
                    dietFilters.add(getResources().getString(R.string.vegan));
                }

                if (dietFilters.size()!=0){
                    currentUser.setDietFilters(dietFilters);
                    currentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e!=null){
                                Log.e(TAG, "error saving account settings: " + e.toString());
                                Toast.makeText(AccountSettingsActivity.this, "error saving account settings", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Toast.makeText(AccountSettingsActivity.this, "account settings saved", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                finish();
            }
        });
    }
}
package com.example.groceriesmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;

import com.example.groceriesmanager.Models.User;
import com.example.groceriesmanager.R;
import com.parse.ParseUser;

public class AccountSettingsActivity extends AppCompatActivity {
    private CheckBox checkboxVegan;
    private CheckBox checkboxVegetarian;
    private CheckBox checkboxGlutenFree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        checkboxGlutenFree = findViewById(R.id.checkboxGlutenFree);
        checkboxVegan = findViewById(R.id.checkboxVegan);
        checkboxVegetarian = findViewById(R.id.checkboxVegetarian);

        User currentUser = (User) ParseUser.getCurrentUser();

        if (currentUser.getDietFilters().contains(getResources().getString(R.string.vegan))){
            // set checkboxVegan as checked
        }
    }
}
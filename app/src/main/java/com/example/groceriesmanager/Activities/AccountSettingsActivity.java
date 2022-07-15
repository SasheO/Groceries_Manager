package com.example.groceriesmanager.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groceriesmanager.Models.User;
import com.example.groceriesmanager.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

public class AccountSettingsActivity extends AppCompatActivity {
    private CheckBox checkboxVegan;
    private CheckBox checkboxVegetarian;
    private CheckBox checkboxGlutenFree;
    private Button btnSave;
    private Button btnCancel;
    TextView tvFiltersLabel;
    LinearLayout llFilters;
    ImageButton ibExpandFilters;
    private static final String TAG = "AccountSettingsActivity";
    public enum dietFiltersEnum {Vegan, Vegetarian, GlutenFree, DairyFree};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        checkboxGlutenFree = findViewById(R.id.checkboxGlutenFree);
        checkboxVegan = findViewById(R.id.checkboxVegan);
        checkboxVegetarian = findViewById(R.id.checkboxVegetarian);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        tvFiltersLabel = findViewById(R.id.tvFiltersLabel);
        ibExpandFilters = findViewById(R.id.ibExpandFilters);
        llFilters = findViewById(R.id.llFilters);

        User currentUser = (User) ParseUser.getCurrentUser();
        EnumSet<dietFiltersEnum> filters = EnumSet.noneOf(dietFiltersEnum.class);

        // if current user specified any of the following as a diet filter, set the checkbox upon opening the page
        if (currentUser.getDietFilters().contains(dietFiltersEnum.Vegan)){
            checkboxVegan.setChecked(true);
            filters.add(dietFiltersEnum.Vegan);
        }
        if (currentUser.getDietFilters().contains(dietFiltersEnum.Vegetarian)){
            checkboxVegetarian.setChecked(true);
            filters.add(dietFiltersEnum.Vegetarian);
        }
        if (currentUser.getDietFilters().contains(dietFiltersEnum.GlutenFree)){
            checkboxGlutenFree.setChecked(true);
            filters.add(dietFiltersEnum.GlutenFree);
        }

        ibExpandFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLlFiltersVisibility();
            }
        });

        tvFiltersLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLlFiltersVisibility();
            }
        });

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
//                    dietFilters.add(getResources().getString(R.string.gluten_free));
                    filters.add(dietFiltersEnum.GlutenFree);
                }
                else {
                    filters.remove(dietFiltersEnum.GlutenFree);
                }
                if (checkboxVegetarian.isChecked()){
//                    dietFilters.add(getResources().getString(R.string.vegetarian));
                    filters.add(dietFiltersEnum.Vegetarian);
                }
                else {
                    filters.remove(dietFiltersEnum.Vegetarian);
                }
                if (checkboxVegan.isChecked()){
//                    dietFilters.add(getResources().getString(R.string.vegan));
                    filters.add(dietFiltersEnum.Vegan);
                }
                else {
                    filters.remove(dietFiltersEnum.Vegan);
                }

//                if (dietFilters.size()!=0){
              if (filters.size()!=0){
//                        currentUser.setDietFilters(dietFilters);
                    currentUser.setDietFilters(filters);
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

    private void switchLlFiltersVisibility() {
        if (llFilters.getVisibility()==View.VISIBLE){
            llFilters.setVisibility(View.GONE);
            ibExpandFilters.setImageResource(R.drawable.expand_arrow);
        }
        else {
            llFilters.setVisibility(View.VISIBLE);
            ibExpandFilters.setImageResource(R.drawable.collapse_arrow);
        }
    }
}
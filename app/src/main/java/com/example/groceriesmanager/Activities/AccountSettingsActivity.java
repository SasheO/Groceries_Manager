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
import com.google.android.flexbox.FlexboxLayout;
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
    private CheckBox checkboxDairyFree;
    private CheckBox checkboxAlcoholFree;
    private CheckBox checkboxImmunoSupportive;
    private CheckBox checkboxKetoFriendly;
    private CheckBox checkboxPescatarian;
    private CheckBox checkboxNoOilAdded;
    private CheckBox checkboxSoyFree;
    private CheckBox checkboxPeanutFree;
    private CheckBox checkboxKosher;
    private CheckBox checkboxPorkFree;
    private Button btnSave;
    private Button btnCancel;
    TextView tvFiltersLabel;
    FlexboxLayout llFilters;
    ImageButton ibExpandFilters;
    EnumSet<dietFiltersEnum> filters;
    private static final String TAG = "AccountSettingsActivity";
    public enum dietFiltersEnum {Vegan, Vegetarian, GlutenFree, DairyFree, AlcoholFree, ImmunoSupportive, KetoFriendly, Pescatarian, NoOilAdded, SoyFree, PeanutFree, Kosher, PorkFree};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        checkboxGlutenFree = findViewById(R.id.checkboxGlutenFree);
        checkboxVegan = findViewById(R.id.checkboxVegan);
        checkboxVegetarian = findViewById(R.id.checkboxVegetarian);
        checkboxDairyFree = findViewById(R.id.checkboxDairyFree);
        checkboxAlcoholFree = findViewById(R.id.checkboxAlcoholFree);
        checkboxImmunoSupportive = findViewById(R.id.checkboxImmunoSupportive);
        checkboxKetoFriendly = findViewById(R.id.checkboxKetoFriendly);
        checkboxPescatarian = findViewById(R.id.checkboxPescatarian);
        checkboxNoOilAdded = findViewById(R.id.checkboxNoOilAdded);
        checkboxSoyFree = findViewById(R.id.checkboxSoyFree);
        checkboxPeanutFree = findViewById(R.id.checkboxPeanutFree);
        checkboxKosher = findViewById(R.id.checkboxKosher);
        checkboxPorkFree = findViewById(R.id.checkboxPorkFree);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        tvFiltersLabel = findViewById(R.id.tvFiltersLabel);
        ibExpandFilters = findViewById(R.id.ibExpandFilters);
        llFilters = findViewById(R.id.llFilters);

        User currentUser = (User) ParseUser.getCurrentUser();
        filters = EnumSet.noneOf(dietFiltersEnum.class);

        setUserFilters(currentUser.getDietFilters());

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

    private void setUserFilters(EnumSet<dietFiltersEnum> userDietFilters){
        // if current user specified any of the following as a diet filter, set the checkbox upon opening the page
        if (userDietFilters.contains(dietFiltersEnum.Vegan)){
            checkboxVegan.setChecked(true);
            filters.add(dietFiltersEnum.Vegan);
        }
        if (userDietFilters.contains(dietFiltersEnum.Vegetarian)){
            checkboxVegetarian.setChecked(true);
            filters.add(dietFiltersEnum.Vegetarian);
        }
        if (userDietFilters.contains(dietFiltersEnum.GlutenFree)){
            checkboxGlutenFree.setChecked(true);
            filters.add(dietFiltersEnum.GlutenFree);
        }
        if (userDietFilters.contains(dietFiltersEnum.DairyFree)){
            checkboxDairyFree.setChecked(true);
            filters.add(dietFiltersEnum.DairyFree);
        }
//        private CheckBox checkboxAlcoholFree;
        if (userDietFilters.contains(dietFiltersEnum.AlcoholFree)){
            checkboxAlcoholFree.setChecked(true);
            filters.add(dietFiltersEnum.AlcoholFree);
        }
//    private CheckBox checkboxImmunoSupportive;
        if (userDietFilters.contains(dietFiltersEnum.ImmunoSupportive)){
            checkboxImmunoSupportive.setChecked(true);
            filters.add(dietFiltersEnum.ImmunoSupportive);
        }
//    private CheckBox checkboxKetoFriendly;
        if (userDietFilters.contains(dietFiltersEnum.KetoFriendly)){
            checkboxKetoFriendly.setChecked(true);
            filters.add(dietFiltersEnum.KetoFriendly);
        }
//    private CheckBox checkboxPescatarian;
        if (userDietFilters.contains(dietFiltersEnum.Pescatarian)){
            checkboxPescatarian.setChecked(true);
            filters.add(dietFiltersEnum.Pescatarian);
        }
//    private CheckBox checkboxNoOilAdded;
        if (userDietFilters.contains(dietFiltersEnum.NoOilAdded)){
            checkboxNoOilAdded.setChecked(true);
            filters.add(dietFiltersEnum.NoOilAdded);
        }
//    private CheckBox checkboxSoyFree;
        if (userDietFilters.contains(dietFiltersEnum.SoyFree)){
            checkboxSoyFree.setChecked(true);
            filters.add(dietFiltersEnum.SoyFree);
        }
//    private CheckBox checkboxPeanutFree;
        if (userDietFilters.contains(dietFiltersEnum.PeanutFree)){
            checkboxPeanutFree.setChecked(true);
            filters.add(dietFiltersEnum.PeanutFree);
        }
//    private CheckBox checkboxKosher;
        if (userDietFilters.contains(dietFiltersEnum.Kosher)){
            checkboxKosher.setChecked(true);
            filters.add(dietFiltersEnum.Kosher);
        }
//    private CheckBox checkboxPorkFree;
        if (userDietFilters.contains(dietFiltersEnum.PorkFree)){
            checkboxPorkFree.setChecked(true);
            filters.add(dietFiltersEnum.PorkFree);
        }
    }
}
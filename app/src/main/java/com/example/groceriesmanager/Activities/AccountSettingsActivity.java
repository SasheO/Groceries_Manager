package com.example.groceriesmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groceriesmanager.Models.User;
import com.example.groceriesmanager.R;
import com.google.android.flexbox.FlexboxLayout;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.EnumSet;

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
    FlexboxLayout flexboxFilters;
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
        flexboxFilters = findViewById(R.id.flexboxFilters);

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

                updateFiltersToSave();

//                if (dietFilters.size()!=0){
              if (filters.size()!=0){
//                        currentUser.setDietFilters(dietFilters);
                    currentUser.setDietFilters(filters);
                }
              else {
                  currentUser.remove(User.KEY_DIETFILTERS);
              }

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

                finish();
            }
        });
    }

    private void switchLlFiltersVisibility() {
        if (flexboxFilters.getVisibility()==View.VISIBLE){
            flexboxFilters.setVisibility(View.GONE);
            ibExpandFilters.setImageResource(R.drawable.expand_arrow);
        }
        else {
            flexboxFilters.setVisibility(View.VISIBLE);
            ibExpandFilters.setImageResource(R.drawable.collapse_arrow);
        }
    }

    // todo: find way to convert "checkbox" + enums to variable name and iterate through enum instead of manually typing out all the if else statements
    private void updateFiltersToSave() {
        if (checkboxGlutenFree.isChecked()){
            filters.add(dietFiltersEnum.GlutenFree);
        }
        else {
            filters.remove(dietFiltersEnum.GlutenFree);
        }
        if (checkboxVegetarian.isChecked()){
            filters.add(dietFiltersEnum.Vegetarian);
        }
        else {
            filters.remove(dietFiltersEnum.Vegetarian);
        }
        if (checkboxVegan.isChecked()){
            filters.add(dietFiltersEnum.Vegan);
        }
        else {
            filters.remove(dietFiltersEnum.Vegan);
        }

        if (checkboxDairyFree.isChecked()){
            filters.add(dietFiltersEnum.DairyFree);
        }
        else {
            filters.remove(dietFiltersEnum.DairyFree);
        }
        if (checkboxAlcoholFree.isChecked()){
            filters.add(dietFiltersEnum.AlcoholFree);
        }
        else {
            filters.remove(dietFiltersEnum.AlcoholFree);
        }
        if (checkboxImmunoSupportive.isChecked()){
            filters.add(dietFiltersEnum.ImmunoSupportive);
        }
        else {
            filters.remove(dietFiltersEnum.ImmunoSupportive);
        }
        if (checkboxKetoFriendly.isChecked()){
            filters.add(dietFiltersEnum.KetoFriendly);
        }
        else {
            filters.remove(dietFiltersEnum.KetoFriendly);
        }
        if (checkboxPescatarian.isChecked()){
            filters.add(dietFiltersEnum.Pescatarian);
        }
        else {
            filters.remove(dietFiltersEnum.Pescatarian);
        }
        if (checkboxNoOilAdded.isChecked()){
            filters.add(dietFiltersEnum.NoOilAdded);
        }
        else {
            filters.remove(dietFiltersEnum.NoOilAdded);
        }
        if (checkboxSoyFree.isChecked()){
            filters.add(dietFiltersEnum.SoyFree);
        }
        else {
            filters.remove(dietFiltersEnum.SoyFree);
        }
        if (checkboxPeanutFree.isChecked()){
            filters.add(dietFiltersEnum.PeanutFree);
        }
        else {
            filters.remove(dietFiltersEnum.PeanutFree);
        }
        if (checkboxKosher.isChecked()){
            filters.add(dietFiltersEnum.Kosher);
        }
        else {
            filters.remove(dietFiltersEnum.Kosher);
        }
        if (checkboxPorkFree.isChecked()){
            filters.add(dietFiltersEnum.PorkFree);
        }
        else {
            filters.remove(dietFiltersEnum.PorkFree);
        }
    }

    // todo: find way to convert "checkbox" + enums to variable name and iterate through enum instead of manually typing out all the if statements
    private void setUserFilters(EnumSet<dietFiltersEnum> userDietFilters){
        if (userDietFilters==null){ // if user has not chosen any filters
            return;
        }
        // if current user specified any of the following as a diet filter, set the checkbox upon opening the page
        if (userDietFilters.contains(dietFiltersEnum.Vegan)){
            checkboxVegan.setChecked(true);
        }
        if (userDietFilters.contains(dietFiltersEnum.Vegetarian)){
            checkboxVegetarian.setChecked(true);
        }
        if (userDietFilters.contains(dietFiltersEnum.GlutenFree)){
            checkboxGlutenFree.setChecked(true);
        }
        if (userDietFilters.contains(dietFiltersEnum.DairyFree)){
            checkboxDairyFree.setChecked(true);
        }
        if (userDietFilters.contains(dietFiltersEnum.AlcoholFree)){
            checkboxAlcoholFree.setChecked(true);
        }
        if (userDietFilters.contains(dietFiltersEnum.ImmunoSupportive)){
            checkboxImmunoSupportive.setChecked(true);
        }
        if (userDietFilters.contains(dietFiltersEnum.KetoFriendly)){
            checkboxKetoFriendly.setChecked(true);
        }
        if (userDietFilters.contains(dietFiltersEnum.Pescatarian)){
            checkboxPescatarian.setChecked(true);
        }
        if (userDietFilters.contains(dietFiltersEnum.NoOilAdded)){
            checkboxNoOilAdded.setChecked(true);
        }
        if (userDietFilters.contains(dietFiltersEnum.SoyFree)){
            checkboxSoyFree.setChecked(true);
        }
        if (userDietFilters.contains(dietFiltersEnum.PeanutFree)){
            checkboxPeanutFree.setChecked(true);
        }
        if (userDietFilters.contains(dietFiltersEnum.Kosher)){
            checkboxKosher.setChecked(true);
        }
        if (userDietFilters.contains(dietFiltersEnum.PorkFree)){
            checkboxPorkFree.setChecked(true);
        }
    }
}
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

import org.apache.commons.lang3.text.WordUtils;

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

              if (filters.size()!=0){
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

    private void updateFiltersToSave() {
        CheckBox v;
        String enumStrValue;

        // check every checkbox in flexbox filters if it is checked
        // add it to the filters to be saved if it is, remove it otherwise
        for (int i = 0; i < flexboxFilters.getChildCount(); i++){
            v = (CheckBox) flexboxFilters.getChildAt(i);

            // format the text from lower-case-separated-with-hyphens to FirstLetterCapitalized
            enumStrValue = v.getText().toString().replaceAll("-", " ");
            enumStrValue = WordUtils.capitalize(enumStrValue);
            enumStrValue = enumStrValue.replaceAll("\\s", "");

            if (v.isChecked()){
                // add to filters
                filters.add(dietFiltersEnum.valueOf(enumStrValue));
            }
            else{
                // remove from filters
                filters.remove(dietFiltersEnum.valueOf(enumStrValue));
            }
        }
    }

    public void setUserFilters(EnumSet<dietFiltersEnum> userDietFilters){
        CheckBox v;

        if (userDietFilters==null){ // if user has not chosen any filters
            for (int i = 0; i < flexboxFilters.getChildCount(); i++) {
                v = (CheckBox) flexboxFilters.getChildAt(i);
                v.setChecked(false);
            }
            return;
        }

        String enumStrValue;

        // check every checkbox in flexboxFilters layout if the enum value is in the given user diet filters
        for (int i = 0; i < flexboxFilters.getChildCount(); i++){
            v = (CheckBox) flexboxFilters.getChildAt(i);

            // format the text from lower-case-separated-with-hyphens to FirstLetterCapitalized
            enumStrValue = v.getText().toString().replaceAll("-", " ");
            enumStrValue = WordUtils.capitalize(enumStrValue);
            enumStrValue = enumStrValue.replaceAll("\\s", "");

            if (userDietFilters.contains(dietFiltersEnum.valueOf(enumStrValue))) {
                // set checkbox checked upon opening page
                v.setChecked(true);
            }
        }
    }
}
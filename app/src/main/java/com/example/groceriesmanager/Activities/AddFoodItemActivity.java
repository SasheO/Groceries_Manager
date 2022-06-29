package com.example.groceriesmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.groceriesmanager.Models.FoodItem;
import com.example.groceriesmanager.Models.User;
import com.example.groceriesmanager.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Objects;

public class AddFoodItemActivity extends AppCompatActivity {
    private Spinner spinnerFoodMeasure;
    private EditText etFoodName;
    private EditText etFoodQty;
    private ImageButton ibAddFoodItem;
    private ImageButton ibExitAddFoodItem;
    private static final String TAG = "AddFoodItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);

        spinnerFoodMeasure = findViewById(R.id.spinnerFoodMeasure);
        etFoodName = findViewById(R.id.etFoodName);
        etFoodQty = findViewById(R.id.etFoodQty);
        ibAddFoodItem = findViewById(R.id.ibAddFoodItem);
        ibExitAddFoodItem = findViewById(R.id.ibExitAddFoodItem);

        String process = getIntent().getStringExtra("process");


        // array adapter for rendering items into the spinner
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.food_measures, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerFoodMeasure.setAdapter(adapter);

        // todo: if intent process is edit, get the food item passed in and set the values in the edit text, etc
        if (Objects.equals(process, "edit")){
            FoodItem foodItem = getIntent().getParcelableExtra("foodItem");
            etFoodName.setText(foodItem.getName());
            etFoodQty.setText(foodItem.getQuantity());
            spinnerFoodMeasure.setSelection(adapter.getPosition(foodItem.getMeasure()));
        }

        ibExitAddFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ibAddFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = getIntent().getStringExtra("type");
                String foodName = etFoodName.getText().toString();
                String foodQty = etFoodQty.getText().toString();

                String foodMeasure = spinnerFoodMeasure.getSelectedItem().toString();
                // Toast.makeText(AddFoodItemActivity.this, foodName +": " + String.valueOf(foodQty) + " " + foodMeasure, Toast.LENGTH_LONG).show();

                if (foodName.replaceAll("\\s+", "").length()==0){ // if the user did not type in a food name or types only spaces
                    Toast.makeText(AddFoodItemActivity.this, "type in the food name", Toast.LENGTH_LONG).show();

                }
                else{
                    if (Objects.equals(process, "new")){
                        addFoodItem(foodName, foodQty, foodMeasure, type);
                    }
                    else{ // process is edit
                        // todo: populate. if process is edit,
                    }
                }
        }
        });
    }

    private void addFoodItem(String foodName, String foodQty, String foodMeasure, String type){

        FoodItem newFoodItem = new FoodItem();
        newFoodItem.setName(foodName.replaceAll("\n", ""));
        newFoodItem.setType(type);
        newFoodItem.setUser(ParseUser.getCurrentUser());
        if (foodQty != ""){
            newFoodItem.setQuantity(foodQty);
            newFoodItem.setMeasure(foodMeasure);
        }
        // update info in parse server
        newFoodItem.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "error saving food item to server");
                }
                else{
                    Log.i(TAG, "food item saved successfully");
                    etFoodName.setText("");
                    etFoodQty.setText("");
                    finish();
                }
            }
        });
        }


}
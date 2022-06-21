package com.example.groceriesmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

public class AddFoodItemActivity extends AppCompatActivity {
    private Spinner spinnerFoodMeasure;
    private EditText etFoodName;
    private EditText etFoodQty;
    private ImageButton btnAddFoodItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);

        spinnerFoodMeasure = findViewById(R.id.spinnerFoodMeasure);
        etFoodName = findViewById(R.id.etFoodName);
        etFoodQty = findViewById(R.id.etFoodQty);
        btnAddFoodItem = findViewById(R.id.btnAddFoodItem);

        // array adapter for rendering items into the spinner
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.food_measures, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerFoodMeasure.setAdapter(adapter);

        btnAddFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = etFoodName.getText().toString();
                int foodQty = Integer.parseInt(etFoodQty.getText().toString());
                String foodMeasure = spinnerFoodMeasure.getSelectedItem().toString();
                // Toast.makeText(AddFoodItemActivity.this, foodName +": " + String.valueOf(foodQty) + " " + foodMeasure, Toast.LENGTH_LONG).show();

                if (foodName.replaceAll("\\s+", "").length()==0){ // if the user did not type in a food name or types only spaces
                    Toast.makeText(AddFoodItemActivity.this, "type in the food name", Toast.LENGTH_LONG).show();

                }
                else{
                    //todo: create a food item, add it to either a grocery or pantry list of the logged in user
                }
            }
        });
    }
}
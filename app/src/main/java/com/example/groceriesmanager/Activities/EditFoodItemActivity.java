package com.example.groceriesmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.groceriesmanager.Adapters.FoodCategorySpinnerAdapter;
import com.example.groceriesmanager.Models.FoodItem;
import com.example.groceriesmanager.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class EditFoodItemActivity extends AppCompatActivity {
    FoodItem foodItem;
    private EditText etFoodQty;
    private EditText etFoodName;
    private static final String TAG = "EditFoodItemActivity";
    // todo: these date integers as today's date
    int selectedYear = 2000;
    int selectedMonth = 5;
    int selectedDayOfMonth = 10;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd", Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food_item);

        etFoodName = findViewById(R.id.etFoodName);
        etFoodQty = findViewById(R.id.etFoodQty);
        Spinner spinnerFoodMeasure = findViewById(R.id.spinnerIngredientMeasure);
        Spinner spinnerFoodCategory = findViewById(R.id.spinnerFoodCategory);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        TextView tvTitle = findViewById(R.id.tvTitle);
        ImageButton ibDatePicker = findViewById(R.id.ibDatePicker);
        ImageButton ibRemoveDate = findViewById(R.id.ibRemoveDate);
        EditText etExpiryDate = findViewById(R.id.etExpiryDate);

        String process = getIntent().getStringExtra("process");


        // array adapter for rendering items into the food measure spinner
        ArrayAdapter<CharSequence> foodMeasureAdapter = ArrayAdapter.createFromResource(this, R.array.food_measures, android.R.layout.simple_spinner_item);
        foodMeasureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerFoodMeasure.setAdapter(foodMeasureAdapter);
        // Our custom Adapter class that we created
        FoodCategorySpinnerAdapter adapter = new FoodCategorySpinnerAdapter(getApplicationContext(), Arrays.asList(getResources().getStringArray(R.array.food_categories)));
        adapter.setDropDownViewResource(R.layout.spinner_item_food_category);
        spinnerFoodCategory.setAdapter(adapter);

        // if intent process is edit, get the food item passed in and set the values in the edit text, etc
        if (Objects.equals(process, "edit")){
            tvTitle.setText("Edit Food Item"); // change title from "create food item"
            foodItem = getIntent().getParcelableExtra("foodItem");
            etFoodName.setText(foodItem.getName());
            etFoodQty.setText(foodItem.getQuantity());
            // todo: fix this spinner measure below. it does not select the food type when opened
            spinnerFoodMeasure.setSelection(foodMeasureAdapter.getPosition(foodItem.getMeasure()));
            // todo: set expiry date if one already set
        }

        ibDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Date Select Listener.
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        // format date to yyyy/mm/dd format
                        String date = String.valueOf(year);
                        if (monthOfYear<10){
                            date = date + "/0" + (monthOfYear + 1);
                        }
                        else {
                            date = date + "/" + (monthOfYear + 1);
                        }
                        if (dayOfMonth<10){
                            date = date + "/0" + (dayOfMonth);
                        }
                        else {
                            date = date + "/" + (dayOfMonth);
                        }

                        etExpiryDate.setText(date);
                        selectedYear = year;
                        selectedMonth = monthOfYear + 1;
                        selectedDayOfMonth = dayOfMonth;
                    }
                };

                // Create DatePickerDialog (Spinner Mode):
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditFoodItemActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        dateSetListener, selectedYear, selectedMonth, selectedDayOfMonth);

                // Show
                datePickerDialog.show();
            }
        });

        ibRemoveDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etExpiryDate.setText(null);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            // todo: save expiry date
            @Override
            public void onClick(View v) {
                LocalDate expiryDate = null;

                String type = getIntent().getStringExtra("type");
                String foodName = etFoodName.getText().toString();
                String foodQty = etFoodQty.getText().toString();
                String expiryDateStr = etExpiryDate.getText().toString();
                if (expiryDateStr!=null){
                    expiryDate = LocalDate.parse(expiryDateStr, formatter);
                }
                String foodMeasure = spinnerFoodMeasure.getSelectedItem().toString();
                String foodCategory = Arrays.asList(getResources().getStringArray(R.array.food_categories)).get(spinnerFoodCategory.getSelectedItemPosition());



                if (foodName.replaceAll("\\s+", "").length()==0){ // if the user did not type in a food name or types only spaces
                    Toast.makeText(EditFoodItemActivity.this, "type in the food name", Toast.LENGTH_LONG).show();

                }
                else if (false){ // todo: set condition date cannot be past or present, must be future

                }
                else{
                    // using foodstruct so we do not need to keep altering the function signature
                    FoodStruct foodStruct = new FoodStruct();
                    foodStruct.foodCategory = foodCategory;
                    foodStruct.foodName = foodName;
                    foodStruct.foodQty = foodQty;
                    foodStruct.foodMeasure = foodMeasure;
                    foodStruct.type = type;
                    foodStruct.expiryDate = expiryDate;
                    if (Objects.equals(process, "new")){ // is user is creating new food item
                        addFoodItem(foodStruct);
                    }
                    else{ // process is edit
                        editFoodItem(foodStruct);
                    }
                }
        }
        });
    }

    class FoodStruct{
        // todo: include expiry date. shoud it be string or date?
        public String foodName;
        public String foodQty;
        public String foodMeasure;
        public String type;
        public String foodCategory;
        public LocalDate expiryDate;
    };

    private void editFoodItem(FoodStruct foodStruct) {
        foodItem.setName(foodStruct.foodName.replaceAll("\n", ""));

        if (!Objects.equals(foodStruct.foodQty, "")){
            foodItem.setQuantity(foodStruct.foodQty);
            foodItem.setMeasure(foodStruct.foodMeasure);
        }
        else{
            foodItem.remove(FoodItem.KEY_MEASURE);
            foodItem.remove(FoodItem.KEY_QUANTITY);
        }

        if (!Objects.equals(foodStruct.foodCategory, "--no selection--")){
            foodItem.setCategory(foodStruct.foodCategory);
        }
        else {
            // if set to no selection, remove food category
            foodItem.remove(FoodItem.KEY_CATEGORY);
        }
        if (foodStruct.expiryDate!=null){
            // todo: save date
        }
        else {
            foodItem.remove(FoodItem.KEY_EXPIRY_DATE);
        }

        foodItem.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "error saving edited food item: " + e.toString());
                    Toast.makeText(EditFoodItemActivity.this, "Could not edit food item", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Prepare data intent to be sent back to grocery/pantry list
                    Intent data = new Intent();
                    // Pass relevant data back as a result
                    data.putExtra("process", "edit");
                    data.putExtra("fooditem", foodItem);
                    // Activity finished ok, return the data
                    setResult(RESULT_OK, data); // set result code and bundle data for response
                    finish();
                }
            }
        });
    }

    private void addFoodItem(FoodStruct foodStruct){

        FoodItem newFoodItem = new FoodItem();
        newFoodItem.setName(foodStruct.foodName.replaceAll("\n", ""));
        newFoodItem.setType(foodStruct.type);
        newFoodItem.setUser(ParseUser.getCurrentUser());
        if (!Objects.equals(foodStruct.foodQty, "")){
            newFoodItem.setQuantity(foodStruct.foodQty);
            newFoodItem.setMeasure(foodStruct.foodMeasure);
        }
        if (!Objects.equals(foodStruct.foodCategory, "--no selection--")){
            newFoodItem.setCategory(foodStruct.foodCategory);
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

                    // Prepare data intent to be sent back to grocery/pantry list
                    Intent data = new Intent();
                    // Pass relevant data back as a result
                    data.putExtra("process", "new");
                    data.putExtra("fooditem", newFoodItem);
                    // Activity finished ok, return the data
                    setResult(RESULT_OK, data); // set result code and bundle data for response
                    finish();
                }
            }
        });
        }


}
package com.example.groceriesmanager;

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
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class AddFoodItemActivity extends AppCompatActivity {
    private Spinner spinnerFoodMeasure;
    private EditText etFoodName;
    private EditText etFoodQty;
    private ImageButton btnAddFoodItem;
    private static final String TAG = "AddFoodItemActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);

        spinnerFoodMeasure = findViewById(R.id.spinnerFoodMeasure);
        etFoodName = findViewById(R.id.etFoodName);
        etFoodQty = findViewById(R.id.etFoodQty);
        btnAddFoodItem = findViewById(R.id.btnAddFoodItem);
        User current_user = (User) ParseUser.getCurrentUser();

        String type = getIntent().getStringExtra("type");


        // array adapter for rendering items into the spinner
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this, R.array.food_measures, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerFoodMeasure.setAdapter(adapter);

        btnAddFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = etFoodName.getText().toString();
                String foodQty = etFoodQty.getText().toString();

                String foodMeasure = spinnerFoodMeasure.getSelectedItem().toString();
                // Toast.makeText(AddFoodItemActivity.this, foodName +": " + String.valueOf(foodQty) + " " + foodMeasure, Toast.LENGTH_LONG).show();

                if (foodName.replaceAll("\\s+", "").length()==0){ // if the user did not type in a food name or types only spaces
                    Toast.makeText(AddFoodItemActivity.this, "type in the food name", Toast.LENGTH_LONG).show();

                }
                else{
                    //todo: create a food item
                    FoodItem newFoodItem = new FoodItem();
                    newFoodItem.setName(foodName.replaceAll("\n", ""));
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

                                // todo: add item to given grocery or pantry list
                                // todo: extract this grocery/pantry list stuff into another function for readability
                                // error: this does not run!!!
                                if (type == "grocery"){
                                    Log.i(TAG, "type: " + type);
                                    List<FoodItem> groceryList = current_user.getGroceryList();
                                    groceryList.add(newFoodItem);
                                    current_user.setGroceryList(groceryList);
                                    current_user.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e!= null){
                                                Log.e(TAG, "error updating user's grocery list: "+e.toString());
                                            }
                                            else {
                                                Log.i(TAG, "successfully updated user grocery list");
                                            }
                                        }
                                    });
                                }
                                else if (type == "pantry"){
                                    Log.i(TAG, "type: " + type);
                                    List<FoodItem> pantryList = current_user.getPantryList();
                                    pantryList.add(newFoodItem);
                                    current_user.setPantryList(pantryList);
                                    current_user.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e!= null){
                                                Log.e(TAG, "error updating user's grocery list: "+e.toString());
                                            }
                                            else {
                                                Log.i(TAG, "successfully updated user grocery list");
                                            }
                                        }
                                    });
                                }

                            }
                        }
                    });



                }
            }
        });
    }

    private void addFoodToGroceryList(String type, FoodItem newFoodItem) {
        User current_user = (User) ParseUser.getCurrentUser();

        // todo: (stretch) check if item with same name exists and update the quantity instead of creating a new object


    }
}
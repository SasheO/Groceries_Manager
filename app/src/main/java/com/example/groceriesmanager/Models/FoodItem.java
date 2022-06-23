package com.example.groceriesmanager.Models;

import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

@ParseClassName("FoodItem")
public class FoodItem extends ParseObject {
    // these variables store the name that will be used to send queries to Parse database
    private static final String KEY_NAME = "name";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_USER = "user";
    private static final String KEY_MEASURE = "measure";
    private static final String TAG = "FoodItem";

    public String getName(){
        try {
            return fetchIfNeeded().getString(KEY_NAME);
        } catch (ParseException e) {
            Log.v(TAG, e.toString());
            return "dummy name";
        }
    }
    public String getQuantity() {
        try {
            return fetchIfNeeded().getString(KEY_QUANTITY);
        } catch (ParseException e) {
            Log.v(TAG, e.toString());
            return "dummy qty";
        }
    }
    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public String getMeasure()
    {
        try {
            return fetchIfNeeded().getString(KEY_MEASURE);
        } catch (ParseException e) {
            Log.v(TAG, e.toString());
            return "dummy measure";
        }
    }

    public void setName(String name){ put(KEY_NAME, name); }
    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }
    public void setQuantity(String quantity){ put(KEY_QUANTITY, quantity); }
    public void setMeasure(String measure){ put(KEY_MEASURE, measure); }

    public void switchList(View view){
        User current_user = (User) ParseUser.getCurrentUser();
            List<FoodItem> groceryList = current_user.getGroceryList();
            List<FoodItem> pantryList = current_user.getPantryList();
            boolean changed = false;
            for (FoodItem foodItem: groceryList){
                if (foodItem.hasSameId(this)){
                    groceryList.remove(foodItem);
                    current_user.setGroceryList(groceryList);
                    changed = true;
                    pantryList.add(foodItem);
                    current_user.setPantryList(pantryList);
                    break;
                }
            }

            if (!changed){
                for (FoodItem foodItem: pantryList){
                    pantryList.remove(foodItem);
                    current_user.setPantryList(pantryList);
                    groceryList.add(foodItem);
                    current_user.setGroceryList(groceryList);
                    break;
                }
            }

        current_user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "error switching food item to other list");
                }
                else{
                    Log.i(TAG, "item switched lists successfully");
                    Snackbar.make(view, getName() + " switched lists!", Snackbar.LENGTH_SHORT).setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switchList(view);
                        }
                    }).show();
                }
            }
        });
    }

    public void delete(View view){
        User current_user = (User) ParseUser.getCurrentUser();
        List<FoodItem> groceryList = current_user.getGroceryList();
        List<FoodItem> pantryList = current_user.getPantryList();
        boolean changed = false;
        for (FoodItem foodItem: groceryList){
            if (foodItem.hasSameId(this)){
                groceryList.remove(foodItem);
                current_user.setGroceryList(groceryList);
                changed = true;
                break;
            }
        }

        if (!changed){
            for (FoodItem foodItem: pantryList){
                pantryList.remove(foodItem);
                current_user.setPantryList(pantryList);
                break;
            }
        }

        current_user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "error deleting food item");
                }
                else{
                    Log.i(TAG, "food item deleted successfully");
                    Snackbar.make(view, getName() + " just deleted!", Snackbar.LENGTH_SHORT).setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // todo: add item back
                        }
                    }).show();
                    // todo: after action bar has disappeared, delete food item in the server
                }
            }
        });

    }
    }

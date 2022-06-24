package com.example.groceriesmanager.Models;

import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.parse.DeleteCallback;
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

    public void switchList(){
        // todo: put this on another thread
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
                if (foodItem.hasSameId(this)){
                    pantryList.remove(foodItem);
                    current_user.setPantryList(pantryList);
                    groceryList.add(foodItem);
                    current_user.setGroceryList(groceryList);
                    break;
                }
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
                }
            }
        });
    }

    public void deleteFood(){ // this removes the food object from the list it is in then deleted the fooditem object in the database
        // todo: put this on another thread

        // todo: ensure that the delete functionality on list works.
        // delete from user list in server
        User current_user = (User) ParseUser.getCurrentUser();
        List<FoodItem> groceryList = current_user.getGroceryList();
        List<FoodItem> pantryList = current_user.getPantryList();
        Log.i(TAG, "food id: " + this.getObjectId());
        for (FoodItem foodItem: groceryList){
            Log.i(TAG, foodItem.getObjectId());
            if (foodItem.hasSameId(this)){
                groceryList.remove(foodItem);
                current_user.setGroceryList(groceryList);
                break;
            }
        }

            for (FoodItem foodItem: pantryList){
                Log.i(TAG, foodItem.getObjectId());
                if (foodItem.hasSameId(this)){
                    pantryList.remove(foodItem);
                    current_user.setPantryList(pantryList);
                    break;
                }
            }


        current_user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "error deleting food item from list in database");
                }
                else{
                    Log.i(TAG, "food item deleted from list successfully in database");

                }
            }
        });

            // delete food item object in server
        deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "error deleting food item object in database");
                }
                else {
                    Log.i(TAG, "food item object successfully deleted in database");
                }
            }
        });
    }

}
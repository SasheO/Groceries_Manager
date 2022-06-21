package com.example.groceriesmanager.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("_User")
public class User extends ParseUser {
    private static final String KEY_GROCERYLIST = "groceryList";
    private static final String KEY_PANTRYLIST = "pantryList";

    public void setGroceryList(List<FoodItem> groceryList){put(KEY_GROCERYLIST, groceryList);}
    public void setPantryList(List<FoodItem> pantryList){put(KEY_PANTRYLIST, pantryList);}

    public List<FoodItem> getGroceryList(){
        if (getList(KEY_GROCERYLIST) == null){
            return new ArrayList<>();
        }
        return getList(KEY_GROCERYLIST);
    }
    public List<FoodItem> getPantryList(){
        if (getList(KEY_PANTRYLIST) == null){
            return new ArrayList<>();
        }
        return getList(KEY_PANTRYLIST);}
}

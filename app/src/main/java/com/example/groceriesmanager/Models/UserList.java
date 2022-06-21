package com.example.groceriesmanager.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("UserList")
public class UserList extends ParseObject {
    private static final String KEY_USER = "user";
    private static final String KEY_TYPE = "type";
    private static final String KEY_FOODITEMS = "foodItems";

    public String getType(){return getString(KEY_TYPE); }
    public List<FoodItem> getFoodItems(){
        if (getList(KEY_FOODITEMS) == null){
            return new ArrayList<>(); // if a list hasn't been created, return an empty list (to prevent bugs in code)
        }
        return getList(KEY_FOODITEMS); }

    public void setType(String type){ put(KEY_TYPE, type); }
    public void setFoodItems(List<FoodItem> foodItems){ put(KEY_FOODITEMS, foodItems); }
}

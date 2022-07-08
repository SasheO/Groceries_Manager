package com.example.groceriesmanager.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("_User")
public class User extends ParseUser {
    private static final String KEY_DIETFILTERS = "dietFilters";
    public void setDietFilters(List<String> dietFilters){put(KEY_DIETFILTERS, dietFilters);}
    public List<String> getDietFilters(){
        if (getList(KEY_DIETFILTERS) == null){
            return new ArrayList<>();
        }
        return getList(KEY_DIETFILTERS);
    }
}

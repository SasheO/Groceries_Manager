package com.example.groceriesmanager.Models;

import android.util.Log;

import com.example.groceriesmanager.Activities.AccountSettingsActivity;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@ParseClassName("_User")
public class User extends ParseUser {
    private static final String TAG = "User";
    private static final String KEY_DIETFILTERS = "dietFilters";
    public void setDietFilters(EnumSet<AccountSettingsActivity.dietFilters> dietFilters){
        List<String> filters = new ArrayList<>();
        for(Enum filter : dietFilters) {
            // do whatever
            filters.add(filter.name());
        }
    put(KEY_DIETFILTERS, filters);}
    public EnumSet<AccountSettingsActivity.dietFilters> getDietFilters(){
        EnumSet<AccountSettingsActivity.dietFilters> filters = EnumSet.noneOf(AccountSettingsActivity.dietFilters.class);
        List<String> filtersList = getList(KEY_DIETFILTERS);
        for (String filter: filtersList){
            filters.add(AccountSettingsActivity.dietFilters.valueOf(filter));
        }
        return filters;
    }
}

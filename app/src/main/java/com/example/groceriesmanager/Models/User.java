package com.example.groceriesmanager.Models;

import com.example.groceriesmanager.Activities.AccountSettingsActivity;
import com.parse.ParseClassName;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@ParseClassName("_User")
public class User extends ParseUser {
    private static final String TAG = "User";
    private static final String KEY_DIETFILTERS = "dietFilters";
    public void setDietFilters(EnumSet<AccountSettingsActivity.dietFiltersEnum> dietFiltersEnum){
        List<String> filters = new ArrayList<>();
        for(Enum filter : dietFiltersEnum) {
            // do whatever
            filters.add(filter.name());
        }
    put(KEY_DIETFILTERS, filters);}
    public EnumSet<AccountSettingsActivity.dietFiltersEnum> getDietFilters(){
        EnumSet<AccountSettingsActivity.dietFiltersEnum> filters = EnumSet.noneOf(AccountSettingsActivity.dietFiltersEnum.class);
        List<String> filtersList = getList(KEY_DIETFILTERS);
        for (String filter: filtersList){
            filters.add(AccountSettingsActivity.dietFiltersEnum.valueOf(filter));
        }
        return filters;
    }
}

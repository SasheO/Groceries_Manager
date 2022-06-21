package com.example.groceriesmanager.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {
    private static final String KEY_GROCERYLIST = "groceryList";
    private static final String KEY_PANTRYLIST = "pantryList";

    public void setGroceryList(UserList groceryList){put(KEY_GROCERYLIST, groceryList);}
    public void setPantryList(UserList pantryList){put(KEY_PANTRYLIST, pantryList);}

    public ParseObject getGroceryList(){return getParseObject(KEY_GROCERYLIST);}
    public ParseObject getPantryList(){return getParseObject(KEY_PANTRYLIST);}

}

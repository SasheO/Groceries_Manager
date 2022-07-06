package com.example.groceriesmanager.Models;

import android.util.Log;

import com.parse.DeleteCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ParseClassName("Recipe")
public class Recipe extends ParseObject {
    private String image_url;
    private String title;
    private List<String> filters;
    private String hyperlink_url; // corresponeds to 'url'
    private List<String> ingredientLines;
    private static final String TAG = "UserProfileFragment";
    private static final String KEY_FILTER_VEGAN = "Vegan";
    private static final String KEY_FILTER_VEGETARIAN = "Vegetarian";
    private static final String KEY_FILTER_GLUTEN_FREE = "Gluten-Free";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_TITLE = "title";
    private static final String KEY_FILTERS = "filters";
    private static final String KEY_USER = "user";
    private static final String KEY_HYPERLINK_URL = "hyperlink_url";
    private static final String KEY_INGREDIENT_LINES = "ingredientLines";

    public Recipe(){}

    public Recipe(JSONObject jsonObject) throws JSONException {
        this.image_url = jsonObject.getJSONObject("recipe").getJSONObject("images").getJSONObject("REGULAR").getString("url");
        this.title = jsonObject.getJSONObject("recipe").getString("label");
        this.hyperlink_url = jsonObject.getJSONObject("recipe").getString("url");
        JSONArray ingredientLinesJSONArray = jsonObject.getJSONObject("recipe").getJSONArray("ingredientLines");
        this.ingredientLines = new ArrayList<>();
        for (int i=0;i<ingredientLinesJSONArray.length();i++){
            this.ingredientLines.add(ingredientLinesJSONArray.getString(i));
        }
        this.filters = new ArrayList<>();
        JSONArray filtersJSONArray = jsonObject.getJSONObject("recipe").getJSONArray("healthLabels");
        for (int i=0; i<filtersJSONArray.length(); i++){
            if (Objects.equals(filtersJSONArray.getString(i), KEY_FILTER_VEGAN)){
                this.filters.add(KEY_FILTER_VEGAN);
            }
            if (Objects.equals(filtersJSONArray.getString(i), KEY_FILTER_VEGETARIAN)){
                this.filters.add(KEY_FILTER_VEGETARIAN);
            }
            if (Objects.equals(filtersJSONArray.getString(i), KEY_FILTER_GLUTEN_FREE)){
                this.filters.add(KEY_FILTER_GLUTEN_FREE);
            }
        }
    }

    public String getImage_url() {
        try {
            return fetchIfNeeded().getString(KEY_IMAGE_URL);
        } catch (ParseException e) {
            Log.v(TAG, e.toString());
            return image_url;
        }
    }


    public String getTitle() {
        try {
            return fetchIfNeeded().getString(KEY_TITLE);
        } catch (ParseException e) {
            Log.v(TAG, e.toString());
            return title;
        }
    }


    public List<String> getFilters() {
        try {
            return fetchIfNeeded().getList(KEY_FILTERS);
        } catch (ParseException e) {
            Log.v(TAG, e.toString());
            return filters;
        }
    }


    public String getHyperlink_url() {
        try {
            return fetchIfNeeded().getString(KEY_HYPERLINK_URL);
        } catch (ParseException e) {
            Log.v(TAG, e.toString());
            return hyperlink_url;
        }
    }


    public List<String> getIngredientLines() {
        try {
            return fetchIfNeeded().getList(KEY_INGREDIENT_LINES);
        } catch (ParseException e) {
            Log.v(TAG, e.toString());
            return ingredientLines;
        }
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setFilters(List<String> filters) {
        this.filters = filters;
    }
    public void setHyperlink_url(String hyperlink_url) {
        this.hyperlink_url = hyperlink_url;
    }
    public void setIngredientLines(List<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
    }
    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }


    public static List<Recipe> fromJsonArray(JSONArray recipeJsonArray) throws JSONException {
        List<Recipe> recipeList = new ArrayList<>();
        for (int i=0; i<recipeJsonArray.length(); i++){
            recipeList.add(new Recipe(recipeJsonArray.getJSONObject(i)));
        }
        return recipeList;

    }

    public void deleteRecipe(){
        deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null){
                    Log.e(TAG, "error deleting recipe in database: " + e.toString());
                }
                else
                {
                    Log.i(TAG, "recipe deleted");
                }
            }
        });
    }

}

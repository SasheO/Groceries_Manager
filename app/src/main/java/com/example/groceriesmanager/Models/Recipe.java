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
    private static final String TAG = "UserProfileFragment";
    private static final String KEY_FILTER_VEGAN = "Vegan";
    private static final String KEY_FILTER_VEGETARIAN = "Vegetarian";
    private static final String KEY_FILTER_GLUTEN_FREE = "Gluten-Free";
    private static final String KEY_IMAGE_URL = "image_url";
    private static final String KEY_TITLE = "title";
    private static final String KEY_FILTERS = "filters";
    private static final String KEY_USER = "user";
    private static final String KEY_HYPERLINK_URL = "hyperlink_url";
    private static final String KEY_INGREDIENT_LINES_STR = "ingredientLines";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_TYPE = "type";
    private static final String KEY_PROCEDURE = "procedure";
    public boolean Saved = false;

    public Recipe(){}

    public Recipe(JSONObject jsonObject) throws JSONException {
        this.image_url = jsonObject.getJSONObject("recipe").getJSONObject("images").getJSONObject("REGULAR").getString("url");
        put(KEY_IMAGE_URL, jsonObject.getJSONObject("recipe").getJSONObject("images").getJSONObject("REGULAR").getString("url"));
        put(KEY_TITLE, jsonObject.getJSONObject("recipe").getString("label"));
        put(KEY_HYPERLINK_URL, jsonObject.getJSONObject("recipe").getString("url"));
        JSONArray ingredientLinesJSONArray = jsonObject.getJSONObject("recipe").getJSONArray("ingredientLines");
        List<String> ingredientLines = new ArrayList<>();
        for (int i=0;i<ingredientLinesJSONArray.length();i++){
            ingredientLines.add(ingredientLinesJSONArray.getString(i));
        }
        put(KEY_INGREDIENT_LINES_STR, ingredientLines);
       List<String> filters = new ArrayList<>();
        JSONArray filtersJSONArray = jsonObject.getJSONObject("recipe").getJSONArray("healthLabels");
        for (int i=0; i<filtersJSONArray.length(); i++){
            if (Objects.equals(filtersJSONArray.getString(i), KEY_FILTER_VEGAN)){
                filters.add(KEY_FILTER_VEGAN);
            }
            if (Objects.equals(filtersJSONArray.getString(i), KEY_FILTER_VEGETARIAN)){
                filters.add(KEY_FILTER_VEGETARIAN);
            }
            if (Objects.equals(filtersJSONArray.getString(i), KEY_FILTER_GLUTEN_FREE)){
                filters.add(KEY_FILTER_GLUTEN_FREE);
            }
        }
        put(KEY_FILTERS, filters);
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
            return null;
        }
    }


    public List<String> getFilters() {
        try {
            return fetchIfNeeded().getList(KEY_FILTERS);
        } catch (ParseException e) {
            Log.v(TAG, e.toString());
            return null;
        }
    }


    public String getHyperlink_url() {
        try {
            return fetchIfNeeded().getString(KEY_HYPERLINK_URL);
        } catch (ParseException e) {
            Log.v(TAG, e.toString());
            return null;
        }
    }


    public List<String> getIngredientLines() {
        try {
            return fetchIfNeeded().getList(KEY_INGREDIENT_LINES_STR);
        } catch (ParseException e) {
            Log.v(TAG, e.toString());
            return null;
        }
    }

    public List<String> getIngredients() {
        try {
            return fetchIfNeeded().getList(KEY_INGREDIENTS);
        } catch (ParseException e) {
            Log.v(TAG, e.toString());
            return null;
        }
    }

    public List<String> getProcedure() {
        try {
            return fetchIfNeeded().getList(KEY_PROCEDURE);
        } catch (ParseException e) {
            Log.v(TAG, e.toString());
            return null;
        }
    }

    public String getType(){
        return getString(KEY_TYPE);
    }

    public void setImage_url(String image_url) {
        put(KEY_IMAGE_URL, image_url);
    }
    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }
    public void setFilters(List<String> filters) {
        put(KEY_FILTERS, filters);
    }
    public void setHyperlink_url(String hyperlink_url) {
        put(KEY_HYPERLINK_URL, hyperlink_url);
    }
    public void setIngredientLines(List<String> ingredientLines) {
        put(KEY_INGREDIENT_LINES_STR, ingredientLines);
    }
    public void setIngredients(List<FoodItem> ingredients) {
        put(KEY_INGREDIENTS, ingredients);
    }
    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }
    public void setType(String type){
        put(KEY_TYPE, type);
    }
    public void setProcedure(List<String> procedure){
        put(KEY_PROCEDURE, procedure);
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

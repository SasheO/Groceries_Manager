package com.example.groceriesmanager.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Recipe {
    private String image_url;
    private String title;
    private List<String> filters;
    private String hyperlink_url; // corresponeds to 'url'
    private List<String> ingredientLines;
    private static final String KEY_FILTER_VEGAN = "Vegan";
    private static final String KEY_FILTER_VEGETARIAN = "Vegetarian";
    private static final String KEY_FILTER_GLUTEN_FREE = "Gluten-Free";

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
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public String getHyperlink_url() {
        return hyperlink_url;
    }

    public void setHyperlink_url(String hyperlink_url) {
        this.hyperlink_url = hyperlink_url;
    }

    public List<String> getIngredientLines() {
        return ingredientLines;
    }

    public void setIngredientLines(List<String> ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    public static List<Recipe> fromJsonArray(JSONArray recipeJsonArray) throws JSONException {
        List<Recipe> recipeList = new ArrayList<>();
        for (int i=0; i<recipeJsonArray.length(); i++){
            recipeList.add(new Recipe(recipeJsonArray.getJSONObject(i)));
        }
        return recipeList;

    }

}

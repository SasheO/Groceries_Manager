package com.example.groceriesmanager.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String image_url;
    private String title;
    private List<String> filters;
    private String hyperlink_url; // corresponeds to 'url'
    private List<String> ingredientLines;

    public Recipe(JSONObject jsonObject) throws JSONException {
        // todo: use get set to create a recipe
        this.image_url = jsonObject.getJSONObject("recipe").getJSONObject("images").getJSONObject("REGULAR").getString("url");
        this.title = jsonObject.getJSONObject("recipe").getString("label");
        this.hyperlink_url = jsonObject.getJSONObject("recipe").getString("url");
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

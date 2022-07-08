package com.example.groceriesmanager.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceriesmanager.Adapters.RecipeSearchAdapter;
//import com.example.groceriesmanager.Lemma;
import com.example.groceriesmanager.Models.Recipe;
import com.example.groceriesmanager.Models.User;
import com.example.groceriesmanager.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecipeSearchFragment extends Fragment {
    ImageButton ibRecipeSearch;
    ImageButton ibRecipeSearchClear;
    EditText etRecipeLookup;
    CheckBox checkboxVegan;
    CheckBox checkboxVegetarian;
    CheckBox checkboxGlutenFree;
    TextView tvNoResultsMessage;
    private static final String TAG = "RecipeSearchFragment";
    public static List<Recipe> recipeList;
    public static List<Recipe> savedRecipesList;
    public RecipeSearchAdapter adapter;
    String userQuery;
    RecyclerView rvRecipeSearch;
    private static final String QUERY_FILTER_VEGAN = "vegan";
    private static final String QUERY_FILTER_VEGETARIAN = "vegetarian";
    private static final String QUERY_FILTER_GLUTEN_FREE = "gluten-free";
    User currentUser;
    List<String> dietFilters;
//    Lemma lemmatizer = new Lemma();

    // required empty constructor
    public RecipeSearchFragment() {}

    public static RecipeSearchFragment newInstance(String userQuery) {
        RecipeSearchFragment fragmentDemo = new RecipeSearchFragment();
        Bundle args = new Bundle();
        args.putString("userQuery", userQuery);
        fragmentDemo.setArguments(args);
        return fragmentDemo;
    }

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_recipe_search, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        etRecipeLookup = (EditText) view.findViewById(R.id.etRecipeLookup);
        ibRecipeSearch = (ImageButton) view.findViewById(R.id.ibRecipeSearch);
        ibRecipeSearchClear = (ImageButton) view.findViewById(R.id.ibRecipeSearchClear);
        rvRecipeSearch = (RecyclerView) view.findViewById(R.id.rvRecipeSearch);
        checkboxVegan = (CheckBox) view.findViewById(R.id.checkboxVegan);
        checkboxVegetarian = (CheckBox) view.findViewById(R.id.checkboxVegetarian);
        checkboxGlutenFree = (CheckBox) view.findViewById(R.id.checkboxGlutenFree);
        tvNoResultsMessage = view.findViewById(R.id.tvNoResultsMessage);
        tvNoResultsMessage.setVisibility(View.GONE);
        recipeList = new ArrayList<>();
        savedRecipesList = new ArrayList<>();
        adapter = new RecipeSearchAdapter(getContext(), recipeList, savedRecipesList);

        currentUser = (User) ParseUser.getCurrentUser();
        dietFilters = currentUser.getDietFilters();

        if (dietFilters.contains(getContext().getResources().getString(R.string.gluten_free))){
            checkboxGlutenFree.setChecked(true);
        }
        if (dietFilters.contains(getContext().getResources().getString(R.string.vegan))){
            checkboxVegan.setChecked(true);
        }
        if (dietFilters.contains(getContext().getResources().getString(R.string.vegetarian))){
            checkboxVegetarian.setChecked(true);
        }

        // in case user is opening this from pantryListFragment
        userQuery = getArguments().getString("userQuery", "");

        // get saved recipes which are passed into adapter
        getSavedRecipes();
        adapter.notifyDataSetChanged();

        // set the adapter on the recycler view
        rvRecipeSearch.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvRecipeSearch.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (!Objects.equals(userQuery, "")){
            searchRecipes(userQuery);
            etRecipeLookup.setText(userQuery);
        }

        // when user clicks on the x to clear search results
        ibRecipeSearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etRecipeLookup.setText("");
                checkboxVegan.setChecked(false);
                checkboxVegetarian.setChecked(false);
                checkboxGlutenFree.setChecked(false);
                tvNoResultsMessage.setVisibility(View.GONE);
                adapter.clear();
            }
        });

        ibRecipeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userQuery = etRecipeLookup.getText().toString();
                // check if user has already typed in something
                if (userQuery.replaceAll("\\s", "").length() == 0){
                    Toast.makeText(getContext(), "type in something!", Toast.LENGTH_LONG).show();
                }
                else {
                    userQuery = etRecipeLookup.getText().toString().trim(); // remove trailing and leading spaces
                    searchRecipes(userQuery);
                }
            }
        });

    }

    public void searchRecipes(String query){
        // check if user has typed in something already
            adapter.clear(); // clear adapter, in case there are already results
            // todo: lemmatize the query
//                    query = lemmatizer.lemmatize(query);
            // send api request to edamam
            OkHttpClient client = new OkHttpClient();
            // this builder helps us to creates the request url
            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.edamam.com/api/recipes/v2").newBuilder();
            urlBuilder.addQueryParameter("q", query);
            urlBuilder.addQueryParameter("type", "public");
            urlBuilder.addQueryParameter("app_id", getResources().getString(R.string.edamam_app_id));
            urlBuilder.addQueryParameter("app_key", getResources().getString(R.string.edamam_app_key));
            if (checkboxVegan.isChecked()){
                urlBuilder.addQueryParameter("health", QUERY_FILTER_VEGAN);
            }
            else if (checkboxVegetarian.isChecked()){ // only add vegetarian if vegan is not already checked
                urlBuilder.addQueryParameter("health", QUERY_FILTER_VEGETARIAN);
            }
            if (checkboxGlutenFree.isChecked()){
                urlBuilder.addQueryParameter("health", QUERY_FILTER_GLUTEN_FREE);
            }
            String url = urlBuilder.build().toString();
            //
            Request request = new Request.Builder()
                    .url(url)
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "error in executing okhttp call: "+ e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()){
                        String myResponse = response.body().string();
                        try {
                            JSONObject responsejson = new JSONObject(myResponse);
                            JSONArray recipesJSONArray = responsejson.getJSONArray("hits");
                            // todo: add all recipes to the recipe list that will be passed into adapter
                            recipeList.addAll(Recipe.fromJsonArray(recipesJSONArray));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "JSONException: " + e.toString());
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // edit the view here
                                adapter.notifyDataSetChanged();
                                if (recipeList.size()==0){
                                    tvNoResultsMessage.setVisibility(View.VISIBLE);
                                }
                                else{
                                    tvNoResultsMessage.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                    else { // response is unsuccessful
                        Log.e(TAG, "response unsuccessful: " + response);
                    }


                }
            });




    }

    public void getSavedRecipes(){
            // specify what type of data we want to query - FoodItem.class
            ParseQuery<Recipe> query = ParseQuery.getQuery(Recipe.class);
            // include data where post is current post
            query.whereEqualTo("type", "saved");
            query.whereEqualTo("user", ParseUser.getCurrentUser());
            // necessary to include non-primitive types
            query.include("user");
            // order posts by creation date (newest first)
            query.findInBackground(new FindCallback<Recipe>() {
                @Override
                public void done(List<Recipe> objects, ParseException e) {
                    if (e!=null){
                        Log.e(TAG, "error retrieving grocery list: " + e.toString());
                    }
                    else{
                        savedRecipesList.addAll(objects);
                        adapter.notifyDataSetChanged();
                    }
                }
            });

    }

}

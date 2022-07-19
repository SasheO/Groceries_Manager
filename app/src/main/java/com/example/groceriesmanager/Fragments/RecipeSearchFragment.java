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

import com.example.groceriesmanager.Activities.AccountSettingsActivity;
import com.example.groceriesmanager.Adapters.RecipeSearchAdapter;
//import com.example.groceriesmanager.Lemma;
import com.example.groceriesmanager.Models.Recipe;
import com.example.groceriesmanager.Models.User;
import com.example.groceriesmanager.R;
import com.google.android.flexbox.FlexboxLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class RecipeSearchFragment extends Fragment {
    private TextView tvExpandFilters;
    private TextView tvResetFilters;
    private ImageButton ibRecipeSearch;
    private ImageButton ibRecipeSearchClear;
    private EditText etRecipeLookup;
    private CheckBox checkboxVegan;
    private CheckBox checkboxVegetarian;
    private CheckBox checkboxGlutenFree;
    private CheckBox checkboxDairyFree;
    private CheckBox checkboxAlcoholFree;
    private CheckBox checkboxImmunoSupportive;
    private CheckBox checkboxKetoFriendly;
    private CheckBox checkboxPescatarian;
    private CheckBox checkboxNoOilAdded;
    private CheckBox checkboxSoyFree;
    private CheckBox checkboxPeanutFree;
    private CheckBox checkboxKosher;
    private CheckBox checkboxPorkFree;
    private FlexboxLayout flexboxFilters;
    private TextView tvNoResultsMessage;
    private static final String TAG = "RecipeSearchFragment";
    public static List<Recipe> recipeList;
    public static List<Recipe> savedRecipesList;
    public RecipeSearchAdapter adapter;
    String userQuery;
    RecyclerView rvRecipeSearch;
    User currentUser;
    EnumSet<AccountSettingsActivity.dietFiltersEnum> filters;
    // these are public because they are also used in youtube search fragment. they are spelt exactly as required in edamam api
    public static final String QUERY_FILTER_VEGAN = "vegan";
    public static final String QUERY_FILTER_VEGETARIAN = "vegetarian";
    public static final String QUERY_FILTER_GLUTEN_FREE = "gluten-free";
    public static final String QUERY_FILTER_DAIRY_FREE = "dairy-free";
    public static final String QUERY_FILTER_ALCOHOL_FREE = "alcohol-free";
    public static final String QUERY_FILTER_IMMUNO_SUPPORTIVE = "immuno-supportive";
    public static final String QUERY_FILTER_KETO_FRIENDLY = "keto-friendly";
    public static final String QUERY_FILTER_PESCATARIAN = "pescatarian";
    public static final String QUERY_FILTER_NO_OIL_ADDED = "no-oil-added";
    public static final String QUERY_FILTER_SOY_FREE = "soy-free";
    public static final String QUERY_FILTER_PEANUT_FREE = "peanut-free";
    public static final String QUERY_FILTER_KOSHER = "kosher";
    public static final String QUERY_FILTER_PORK_FREE = "pork-free";
    InputStream dictLemmatizer = null;
    DictionaryLemmatizer lemmatizer;
    POSTaggerME tagger;
    private String next_page;

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
        tvExpandFilters = view.findViewById(R.id.tvExpandFilters);
        tvResetFilters = view.findViewById(R.id.tvResetFilters);
        flexboxFilters = view.findViewById(R.id.flexboxFilters);
        etRecipeLookup = view.findViewById(R.id.etRecipeLookup);
        ibRecipeSearch = view.findViewById(R.id.ibRecipeSearch);
        ibRecipeSearchClear = view.findViewById(R.id.ibRecipeSearchClear);
        rvRecipeSearch = view.findViewById(R.id.rvRecipeSearch);
        checkboxGlutenFree = view.findViewById(R.id.checkboxGlutenFree);
        checkboxVegan = view.findViewById(R.id.checkboxVegan);
        checkboxVegetarian = view.findViewById(R.id.checkboxVegetarian);
        checkboxDairyFree = view.findViewById(R.id.checkboxDairyFree);
        checkboxPorkFree = view.findViewById(R.id.checkboxPorkFree);
        checkboxAlcoholFree = view.findViewById(R.id.checkboxAlcoholFree);
        checkboxImmunoSupportive = view.findViewById(R.id.checkboxImmunoSupportive);
        checkboxKetoFriendly = view.findViewById(R.id.checkboxKetoFriendly);
        checkboxPescatarian = view.findViewById(R.id.checkboxPescatarian);
        checkboxNoOilAdded = view.findViewById(R.id.checkboxNoOilAdded);
        checkboxSoyFree = view.findViewById(R.id.checkboxSoyFree);
        checkboxPeanutFree = view.findViewById(R.id.checkboxPeanutFree);
        checkboxKosher = view.findViewById(R.id.checkboxKosher);
        tvNoResultsMessage = view.findViewById(R.id.tvNoResultsMessage);
        tvNoResultsMessage.setVisibility(View.GONE);
        recipeList = new ArrayList<>();
        savedRecipesList = new ArrayList<>();
        adapter = new RecipeSearchAdapter(getContext(), recipeList, savedRecipesList);

        currentUser = (User) ParseUser.getCurrentUser();
        filters = currentUser.getDietFilters();
        // todo: move the below into a method and add more filters populating
        setUserFilters();

        // in case user is opening this from pantryListFragment
        userQuery = getArguments().getString("userQuery", "");

        // get saved recipes which are passed into adapter
        getSavedRecipes();
        adapter.notifyDataSetChanged();

        // instantiate dictionary lemmatizer
        try {
            dictLemmatizer = getContext().getResources().openRawResource(R.raw.en_lemmatizer_dict);
            lemmatizer = new DictionaryLemmatizer(dictLemmatizer);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "dictLemmatizer error: " + e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "lemmatizer error: " + e.toString());
            e.printStackTrace();
        }

        // create tagger for getting parts of speech per word
        // here: https://opennlp.apache.org/docs/2.0.0/manual/opennlp.html#tools.postagger.tagging.cmdline
        try {
            // todo: replace en_pos_maxent with a valid file with the right format
            InputStream tokenModelIn = getContext().getAssets().open("en_pos_maxent.bin");
            // keeps getting 'java.lang.String java.util.Properties.getProperty(java.lang.String)' on a null object reference
//            if (tokenModelIn!=null){
//                POSModel model = new POSModel(tokenModelIn);
//                tagger = new POSTaggerME(model);
//            }
//            else{
//                Log.e(TAG, "tokenModelIn == null");
//            }

        } catch (IOException e) {
            Log.e(TAG, "IOException tokenModelIn: " + e.toString());
            e.printStackTrace();
        }

        // set the adapter on the recycler view
        rvRecipeSearch.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvRecipeSearch.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (!Objects.equals(userQuery, "")){
            searchRecipes(userQuery);
            etRecipeLookup.setText(userQuery);
        }

        tvExpandFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flexboxFilters.getVisibility()==View.GONE){
                    flexboxFilters.setVisibility(View.VISIBLE);
                    tvExpandFilters.setText("Close filters");
                }
                else {
                    flexboxFilters.setVisibility(View.GONE);
                    tvExpandFilters.setText("Edit filters");}
            }
        });

        tvResetFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filters = currentUser.getDietFilters();
                setUserFilters();
            }
        });

        // when user clicks on the x to clear search results
        ibRecipeSearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etRecipeLookup.setText("");
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

    private void searchRecipes(String query){
        // convert string to string array gotten from here: https://stackoverflow.com/questions/4674850/converting-a-sentence-string-to-a-string-array-of-words-in-java
        String[] queryStringArray = query.split("\\s+");
        for (int i = 0; i < queryStringArray.length; i++) {
            // You may want to check for a non-word character before blindly
            // performing a replacement
            // It may also be necessary to adjust the character class
            queryStringArray[i] = queryStringArray[i].replaceAll("[^\\w]", "");
        }

//        String[] posTags = tagger.tag(queryStringArray);

//        String[] lemmas = lemmatizer.lemmatize(queryStringArray, posTags);


        // check if user has typed in something already
            adapter.clear(); // clear adapter, in case there are already results

            // send api request to edamam
            OkHttpClient client = new OkHttpClient();
            // this builder helps us to creates the request url
            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.edamam.com/api/recipes/v2").newBuilder();
            urlBuilder.addQueryParameter("q", query);
            urlBuilder.addQueryParameter("type", "public");
            urlBuilder.addQueryParameter("app_id", getResources().getString(R.string.edamam_app_id));
            urlBuilder.addQueryParameter("app_key", getResources().getString(R.string.edamam_app_key));

            // todo: convert this to for loop instead of manually typing out all these
            if (checkboxVegan.isChecked()){urlBuilder.addQueryParameter("health", QUERY_FILTER_VEGAN);}
            // only add vegetarian if not alreay checked
            else if (checkboxVegetarian.isChecked()){ urlBuilder.addQueryParameter("health", QUERY_FILTER_VEGETARIAN);}
            if (checkboxGlutenFree.isChecked()){ urlBuilder.addQueryParameter("health", QUERY_FILTER_GLUTEN_FREE);}
            if (checkboxDairyFree.isChecked()){ urlBuilder.addQueryParameter("health", QUERY_FILTER_DAIRY_FREE);}
            if (checkboxAlcoholFree.isChecked()){ urlBuilder.addQueryParameter("health", QUERY_FILTER_ALCOHOL_FREE);}
            if (checkboxImmunoSupportive.isChecked()){ urlBuilder.addQueryParameter("health", QUERY_FILTER_IMMUNO_SUPPORTIVE);}
            if (checkboxKetoFriendly.isChecked()){ urlBuilder.addQueryParameter("health", QUERY_FILTER_KETO_FRIENDLY);}
            if (checkboxPescatarian.isChecked()){ urlBuilder.addQueryParameter("health", QUERY_FILTER_PESCATARIAN);}
            if (checkboxNoOilAdded.isChecked()){ urlBuilder.addQueryParameter("health", QUERY_FILTER_NO_OIL_ADDED);}
            if (checkboxSoyFree.isChecked()){ urlBuilder.addQueryParameter("health", QUERY_FILTER_SOY_FREE);}
            if (checkboxPeanutFree.isChecked()){ urlBuilder.addQueryParameter("health", QUERY_FILTER_PEANUT_FREE);}
            if (checkboxKosher.isChecked()){ urlBuilder.addQueryParameter("health", QUERY_FILTER_KOSHER);}
            if (checkboxPorkFree.isChecked()){ urlBuilder.addQueryParameter("health", QUERY_FILTER_PORK_FREE);}

            String url = urlBuilder.build().toString();
        Log.i(TAG, "url: " + url);
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
                            next_page = responsejson.getJSONObject("_links").getJSONObject("next").getString("href");
                            Log.i(TAG, "next page:" + next_page);
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

    // todo: make this into a loop
    private void setUserFilters(){
        if (filters==null){ // if user has not chosen any filters
            return;
        }
        // if current user specified any of the following as a diet filter, set the checkbox upon opening the page
        if (filters.contains(AccountSettingsActivity.dietFiltersEnum.Vegan)){
            checkboxVegan.setChecked(true);
        }
        else {
            checkboxVegan.setChecked(false);
        }
        if (filters.contains(AccountSettingsActivity.dietFiltersEnum.Vegetarian)){
            checkboxVegetarian.setChecked(true);
        }
        else {
            checkboxVegetarian.setChecked(false);
        }
        if (filters.contains(AccountSettingsActivity.dietFiltersEnum.GlutenFree)){
            checkboxGlutenFree.setChecked(true);
        }
        else {
            checkboxGlutenFree.setChecked(false);
        }
        if (filters.contains(AccountSettingsActivity.dietFiltersEnum.DairyFree)){
            checkboxDairyFree.setChecked(true);
        }
        else {
            checkboxDairyFree.setChecked(false);
        }
        if (filters.contains(AccountSettingsActivity.dietFiltersEnum.AlcoholFree)){
            checkboxAlcoholFree.setChecked(true);
        }
        else {
            checkboxAlcoholFree.setChecked(false);
        }
        if (filters.contains(AccountSettingsActivity.dietFiltersEnum.ImmunoSupportive)){
            checkboxImmunoSupportive.setChecked(true);
        }
        else {
            checkboxImmunoSupportive.setChecked(false);
        }
        if (filters.contains(AccountSettingsActivity.dietFiltersEnum.KetoFriendly)){
            checkboxKetoFriendly.setChecked(true);
        }
        else {
            checkboxKetoFriendly.setChecked(false);
        }
        if (filters.contains(AccountSettingsActivity.dietFiltersEnum.Pescatarian)){
            checkboxPescatarian.setChecked(true);
        }
        else {
            checkboxPescatarian.setChecked(false);
        }
        if (filters.contains(AccountSettingsActivity.dietFiltersEnum.NoOilAdded)){
            checkboxNoOilAdded.setChecked(true);
        }
        else {
            checkboxNoOilAdded.setChecked(false);
        }
        if (filters.contains(AccountSettingsActivity.dietFiltersEnum.SoyFree)){
            checkboxSoyFree.setChecked(true);
        }
        else {
            checkboxSoyFree.setChecked(false);
        }
        if (filters.contains(AccountSettingsActivity.dietFiltersEnum.PeanutFree)){
            checkboxPeanutFree.setChecked(true);
        }
        else {
            checkboxPeanutFree.setChecked(false);
        }
        if (filters.contains(AccountSettingsActivity.dietFiltersEnum.Kosher)){
            checkboxKosher.setChecked(true);
        }
        else {
            checkboxKosher.setChecked(false);
        }
        if (filters.contains(AccountSettingsActivity.dietFiltersEnum.PorkFree)){
            checkboxPorkFree.setChecked(true);
        }
        else {
            checkboxPorkFree.setChecked(false);
        }
    }

    // helper function to open bin files (e.g. en_pos_maxent.bin which are used in pos tagging for lemmatizer)
    // gotten from https://stackoverflow.com/questions/29602728/adding-bin-files-in-android-studio
    public InputStream openBin(String filename) throws IOException {
        ClassLoader classLoader = getContext().getClass().getClassLoader();
        File file = new File(classLoader.getResource(filename).getPath());
        InputStream input = new FileInputStream(file);
        return input;
    }

}

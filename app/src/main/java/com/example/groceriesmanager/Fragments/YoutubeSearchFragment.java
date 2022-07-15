package com.example.groceriesmanager.Fragments;

import android.content.Context;
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
import com.example.groceriesmanager.Activities.MainActivity;
import com.example.groceriesmanager.Adapters.VideoSearchAdapter;
import com.example.groceriesmanager.Models.User;
import com.example.groceriesmanager.Models.Video;
import com.example.groceriesmanager.R;
import com.google.android.flexbox.FlexboxLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YoutubeSearchFragment extends Fragment {
    private TextView tvExpandFilters;
    private TextView tvResetFilters;
    private EditText etYoutubeLookup;
    private ImageButton ibYoutubeSearchClear;
    private ImageButton ibYoutubeSearch;
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
    private static final String TAG = "YoutubeSearchFragment";
    public static List<Video> videoList;
    public VideoSearchAdapter adapter;
    RecyclerView rvYoutubeSearch;
    private User currentUser;
    EnumSet<AccountSettingsActivity.dietFiltersEnum> filters;
    public static List<Video> savedVideosList;

    // required empty constructor
    public YoutubeSearchFragment() {}

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_youtube_search, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        tvExpandFilters = view.findViewById(R.id.tvExpandFilters);
        tvResetFilters = view.findViewById(R.id.tvResetFilters);
        flexboxFilters = view.findViewById(R.id.flexboxFilters);
        etYoutubeLookup = (EditText) view.findViewById(R.id.etRecipeLookup);
        ibYoutubeSearch = (ImageButton) view.findViewById(R.id.ibRecipeSearch);
        ibYoutubeSearchClear = (ImageButton) view.findViewById(R.id.ibRecipeSearchClear);
        rvYoutubeSearch = (RecyclerView) view.findViewById(R.id.rvRecipeSearch);
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
        videoList = new ArrayList<>();
        savedVideosList = new ArrayList<>();
        adapter = new VideoSearchAdapter(getContext(), videoList, savedVideosList);

        // set the adapter on the recycler view
        rvYoutubeSearch.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvYoutubeSearch.setLayoutManager(new LinearLayoutManager(getActivity()));

        currentUser = (User) ParseUser.getCurrentUser();
        filters = currentUser.getDietFilters();

        setUserFilters();

        // get saved recipes which are passed into adapter
        getSavedVideos();
        adapter.notifyDataSetChanged();

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
        ibYoutubeSearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etYoutubeLookup.setText("");
                adapter.clear();
            }
        });

        ibYoutubeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if user has typed in something already
                String userQuery = etYoutubeLookup.getText().toString();
                if (userQuery.replaceAll("\\s", "").length() == 0){
                    Toast.makeText(getContext(), "type in something!", Toast.LENGTH_LONG).show();
                }
                else{
                    adapter.clear(); // clear adapter, in case there are already results
                    // send api request to Youtube: check here https://developers.google.com/youtube/v3/docs/search/listn
                    OkHttpClient client = new OkHttpClient();
                    // this builder helps us to creates the request url
                    HttpUrl.Builder urlBuilder = HttpUrl.parse("https://www.googleapis.com/youtube/v3/search").newBuilder();
                    urlBuilder.addQueryParameter("part", "snippet"); // required value
                    urlBuilder.addQueryParameter("maxResults", "20");
                    urlBuilder.addQueryParameter("order", "relevance");
                    urlBuilder.addQueryParameter("type", "video");
                    String query = getYoutubeApiQuery();
                    urlBuilder.addQueryParameter("q", query + " recipe"); // specify  that you are searchign for recipe
                    urlBuilder.addQueryParameter("key", getResources().getString(R.string.youtube_api_key));
                    String url = urlBuilder.build().toString();
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
                                Log.i(TAG, "url: " + url);
                                Log.i(TAG, "okhttp call successfully executed");
                                String myResponse = response.body().string();
                                try {
                                    JSONObject responsejson = new JSONObject(myResponse);
                                    JSONArray videoJSONArray = responsejson.getJSONArray("items");
                                    videoList.addAll(Video.fromJsonArray(videoJSONArray));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e(TAG, "JSONException: " + e.toString());
                                }

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // edit the view here
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                            else { // response is unsuccessful
                                Log.e(TAG, "response unsuccessful: " + response);
                            }


                        }
                    });



                }
            }
        });
    }


    public void getSavedVideos(){
        // specify what type of data we want to query - Video.class
        ParseQuery<Video> query = ParseQuery.getQuery(Video.class);
        // include data where user is current user
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        // necessary to include non-primitive types
        query.include("user");
        // order posts by creation date (newest first)
        query.findInBackground(new FindCallback<Video>() {
            @Override
            public void done(List<Video> objects, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "error retrieving grocery list: " + e.toString());
                }
                else{
                    savedVideosList.addAll(objects);
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    private String getYoutubeApiQuery() {
        String query = ""; // remove any leading and trailing spaces
        if (checkboxVegan.isChecked()){
            query = query + " " + RecipeSearchFragment.QUERY_FILTER_VEGAN;
        }
        else if (checkboxVegetarian.isChecked()){ // only include vegetarian if vegan isn't already checked
            query = query + " " + RecipeSearchFragment.QUERY_FILTER_VEGETARIAN;
        }
        if (checkboxGlutenFree.isChecked()){
            query = query + " " + RecipeSearchFragment.QUERY_FILTER_GLUTEN_FREE;
        }
        if (checkboxDairyFree.isChecked()){
            query = query + " " + RecipeSearchFragment.QUERY_FILTER_DAIRY_FREE;
        }
        if (checkboxAlcoholFree.isChecked()){
            query = query + " " + RecipeSearchFragment.QUERY_FILTER_ALCOHOL_FREE;
        }
        if (checkboxImmunoSupportive.isChecked()){
            query = query + " " + RecipeSearchFragment.QUERY_FILTER_IMMUNO_SUPPORTIVE;
        }
        if (checkboxKetoFriendly.isChecked()){
            query = query + " " + RecipeSearchFragment.QUERY_FILTER_KETO_FRIENDLY;
        }
        if (checkboxPescatarian.isChecked()){
            query = query + " " + RecipeSearchFragment.QUERY_FILTER_PESCATARIAN;
        }
        if (checkboxNoOilAdded.isChecked()){
            query = query + " " + RecipeSearchFragment.QUERY_FILTER_NO_OIL_ADDED;
        }
        if (checkboxSoyFree.isChecked()){
            query = query + " " + RecipeSearchFragment.QUERY_FILTER_SOY_FREE;
        }
        if (checkboxPeanutFree.isChecked()){
            query = query + " " + RecipeSearchFragment.QUERY_FILTER_PEANUT_FREE;
        }
        if (checkboxKosher.isChecked()){
            query = query + " " + RecipeSearchFragment.QUERY_FILTER_KOSHER;
        }
        if (checkboxPorkFree.isChecked()){
            query = query + " " + RecipeSearchFragment.QUERY_FILTER_PORK_FREE;
        }

        query = query + " " + etYoutubeLookup.getText().toString().trim() + " recipe";
        return query;
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

}

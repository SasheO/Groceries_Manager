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

import org.apache.commons.lang3.text.WordUtils;
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
        String query = "";
        // check each textbox in flexbox filters if it is checkes, and add it to the query
        CheckBox v;
        for (int i = 0; i < flexboxFilters.getChildCount(); i++){
            v = (CheckBox) flexboxFilters.getChildAt(i);

            if (v.isChecked()){
                // add text to url, checkbox text attributes are formatted already how it is in edamam documentation
                query = query + " " + v.getText().toString();
            }

        }
                query = query + " " + etYoutubeLookup.getText().toString().trim() + " recipe";
        return query;
    }

    private void setUserFilters(){

        CheckBox v;
        if (filters==null){ // if user has not chosen any filters
            // uncheck all boxes
            for (int i = 0; i < flexboxFilters.getChildCount(); i++) {
                v = (CheckBox) flexboxFilters.getChildAt(i);
                v.setChecked(false);
            }
            return;
        }

        String enumStrValue;

        // check every checkbox in flexboxFilters layout if the enum value is in the given user diet filters
        for (int i = 0; i < flexboxFilters.getChildCount(); i++){
            v = (CheckBox) flexboxFilters.getChildAt(i);

            // format the text from lower-case-separated-with-hyphens to FirstLetterCapitalized
            enumStrValue = v.getText().toString().replaceAll("-", " ");
            enumStrValue = WordUtils.capitalize(enumStrValue);
            enumStrValue = enumStrValue.replaceAll("\\s", "");

            if (filters.contains(AccountSettingsActivity.dietFiltersEnum.valueOf(enumStrValue))) {
                // set checkbox checked upon opening page
                v.setChecked(true);
            }
            else {
                v.setChecked(false);
            }
        }
    }

}

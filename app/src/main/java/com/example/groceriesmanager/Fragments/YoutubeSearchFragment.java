package com.example.groceriesmanager.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceriesmanager.Adapters.RecipeAdapter;
import com.example.groceriesmanager.Adapters.VideoAdapter;
import com.example.groceriesmanager.Models.Recipe;
import com.example.groceriesmanager.Models.Video;
import com.example.groceriesmanager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YoutubeSearchFragment extends Fragment {
    EditText etYoutubeLookup;
    ImageButton ibYoutubeSearchClear;
    ImageButton ibYoutubeSearch;
    CheckBox checkboxVegan;
    CheckBox checkboxVegetarian;
    CheckBox checkboxGlutenFree;
    private static final String TAG = "YoutubeSearchFragment";
    public static List<Video> videoList;
    public VideoAdapter adapter;
    RecyclerView rvYoutubeSearch;
    private static final String QUERY_FILTER_VEGAN = "vegan";
    private static final String QUERY_FILTER_VEGETARIAN = "vegetarian";
    private static final String QUERY_FILTER_GLUTEN_FREE = "gluten free";

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
        etYoutubeLookup = (EditText) view.findViewById(R.id.etYoutubeLookup);
        ibYoutubeSearch = (ImageButton) view.findViewById(R.id.ibYoutubeSearch);
        ibYoutubeSearchClear = (ImageButton) view.findViewById(R.id.ibYoutubeSearchClear);
        rvYoutubeSearch = (RecyclerView) view.findViewById(R.id.rvYoutubeSearch);
        checkboxVegan = (CheckBox) view.findViewById(R.id.checkboxVegan);
        checkboxVegetarian = (CheckBox) view.findViewById(R.id.checkboxVegetarian);
        checkboxGlutenFree = (CheckBox) view.findViewById(R.id.checkboxGlutenFree);
        videoList = new ArrayList<>();
        adapter = new VideoAdapter(getContext(), videoList);

        // set the adapter on the recycler view
        rvYoutubeSearch.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvYoutubeSearch.setLayoutManager(new LinearLayoutManager(getActivity()));

        // when user clicks on the x to clear search results
        ibYoutubeSearchClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etYoutubeLookup.setText("");
                checkboxVegan.setChecked(false);
                checkboxVegetarian.setChecked(false);
                checkboxGlutenFree.setChecked(false);
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
                    String query = etYoutubeLookup.getText().toString();
                    // send api request to Youtube: check here https://developers.google.com/youtube/v3/docs/search/listn
                    OkHttpClient client = new OkHttpClient();
                    // this builder helps us to creates the request url
                    HttpUrl.Builder urlBuilder = HttpUrl.parse("https://www.googleapis.com/youtube/v3/search").newBuilder();
                    urlBuilder.addQueryParameter("part", "snippet"); // required value
                    urlBuilder.addQueryParameter("maxResults", "20");
                    urlBuilder.addQueryParameter("order", "relevance");
                    urlBuilder.addQueryParameter("type", "video");
                    if (checkboxVegan.isChecked()){
                        query = query + " " + QUERY_FILTER_VEGAN;
                    }
                    if (checkboxVegetarian.isChecked()){
                        query = query + " " + QUERY_FILTER_VEGETARIAN;
                    }
                    if (checkboxGlutenFree.isChecked()){
                        query = query + " " + QUERY_FILTER_GLUTEN_FREE;
                    }
                    urlBuilder.addQueryParameter("q", query);
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
//                                String myResponse = response.body().string();
//                                try {
//                                    JSONObject responsejson = new JSONObject(myResponse);
//                                    JSONArray recipesJSONArray = responsejson.getJSONArray("hits");
//                                    // todo: add all recipes to the recipe list that will be passed into adapter
//                                    videoList.addAll(Video.fromJsonArray(recipesJSONArray));
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                    Log.e(TAG, "JSONException: " + e.toString());
//                                }
//
//                                getActivity().runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        // edit the view here
//                                        adapter.notifyDataSetChanged();
//                                    }
//                                });
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
}

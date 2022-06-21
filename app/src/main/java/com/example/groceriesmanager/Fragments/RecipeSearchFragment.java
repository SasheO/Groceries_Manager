package com.example.groceriesmanager.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.TextHttpResponseHandler;
import com.example.groceriesmanager.R;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class RecipeSearchFragment extends Fragment {

    // required empty constructor
    public RecipeSearchFragment() {}

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
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);

        // todo: create async client for edamam
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
//        params.put("q", ""); // q is whatever the user searches
//        params.put("app_id", R.string.edamam_app_id);
//        params.put("app_key", R.string.edamam_app_key);
//        List<String> filters = new ArrayList<>();
//        params.put("health", filters); // health is an array string that can hold filters like vegan, vegetarian, etc.
        client.get("https://api.edamam.com/api/recipes/v2", params, new TextHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, String response) {
                        // called when response HTTP status is "200 OK"
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String errorResponse, Throwable t) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    }
                }
        );

    }
}

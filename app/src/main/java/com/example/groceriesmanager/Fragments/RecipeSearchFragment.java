package com.example.groceriesmanager.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

//import com.codepath.asynchttpclient.AsyncHttpClient;
//import com.codepath.asynchttpclient.RequestParams;
//import com.codepath.asynchttpclient.callback.TextHttpResponseHandler;
import com.example.groceriesmanager.R;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class RecipeSearchFragment extends Fragment {
    ImageButton ibRecipeSearch;
    EditText etRecipeIngredient;
    private static final String TAG = "RecipeSearchFragment";

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
        etRecipeIngredient = (EditText) view.findViewById(R.id.etRecipeIngredient);
        ibRecipeSearch = (ImageButton) view.findViewById(R.id.ibRecipeSearch);

        ibRecipeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: check if user has typed in ingredients
                String userQuery = etRecipeIngredient.getText().toString();
                if (userQuery.replaceAll("\\s", "").length() == 0){
                    Toast.makeText(getContext(), "type in something!", Toast.LENGTH_LONG).show();
                }
                else{
//                // todo: create async client for edamam
//                AsyncHttpClient client = new AsyncHttpClient();
//                RequestParams params = new RequestParams();
//        params.put("q", userQuery); // q is whatever the user searches
//        params.put("app_id", R.string.edamam_app_id);
//        params.put("app_key", R.string.edamam_app_key);
//        List<String> filters = new ArrayList<>();
//        params.put("health", filters); // health is an array string that can hold filters like vegan, vegetarian, etc.
//                client.get("https://api.edamam.com/api/recipes/v2", params, new TextHttpResponseHandler() {
//                            @Override
//                            public void onSuccess(int statusCode, Headers headers, String response) {
//                                // called when response HTTP status is "200 OK"
//                                Log.i(TAG, "response: "+response);
//                            }
//
//                            @Override
//                            public void onFailure(int statusCode, Headers headers, String errorResponse, Throwable t) {
//                                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
//                                Log.e(TAG, "unable to access from edamam server. error: " + errorResponse);
//                            }
//                        }
//                );
                }
            }
        });


    }
}

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

import com.example.groceriesmanager.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
                    String query = etRecipeIngredient.getText().toString();
                    // todo: finish implementing okhttp client for edamam
                    OkHttpClient client = new OkHttpClient();
                    // this creates the request url
                    HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.edamam.com/api/recipes/v2").newBuilder();
                    urlBuilder.addQueryParameter("q", query);
                    urlBuilder.addQueryParameter("app_id", getResources().getString(R.string.edamam_app_id));
                    urlBuilder.addQueryParameter("app_key", getResources().getString(R.string.edamam_app_key));
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
                                Log.i(TAG, "response: " + myResponse);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // edit the view here
                                    }
                                });
                            }
                            else { // response is unsuccessful
                                Log.e(TAG, "response unsuccessful: " + response);
                            }


                        }
                    });



//                AsyncHttpClient client = new AsyncHttpClient();
//                RequestParams params = new RequestParams();
//        params.put("q", userQuery); // q is whatever the user searches

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

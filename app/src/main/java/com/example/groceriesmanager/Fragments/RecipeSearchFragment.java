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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceriesmanager.Adapters.FoodListAdapter;
import com.example.groceriesmanager.Adapters.RecipeAdapter;
import com.example.groceriesmanager.Models.Recipe;
import com.example.groceriesmanager.R;

import org.json.JSONArray;
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
    public static List<Recipe> recipeList;
    public RecipeAdapter adapter;
    RecyclerView rvRecipeSearch;

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
        rvRecipeSearch = (RecyclerView) view.findViewById(R.id.rvRecipeSearch);
        recipeList = new ArrayList<>();
        adapter = new RecipeAdapter(getContext(), recipeList);

        // set the adapter on the recycler view
        rvRecipeSearch.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvRecipeSearch.setLayoutManager(new LinearLayoutManager(getActivity()));


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
                    urlBuilder.addQueryParameter("type", "public");
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
                                Log.i(TAG, "recipes: " + myResponse);
                                try {
                                    JSONObject responsejson = new JSONObject(myResponse);
                                    JSONArray recipesJSONArray = responsejson.getJSONArray("hits");
                                    // todo: add all recipes to the recipe list that will be passed into adapter
                                    recipeList.addAll(Recipe.fromJsonArray(recipesJSONArray));
                                    Log.i(TAG, "recipe list: " + recipeList.toString());
                                    Log.i(TAG, String.valueOf(recipeList.size()));
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
}

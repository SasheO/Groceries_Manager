package com.example.groceriesmanager.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceriesmanager.Adapters.RecipeAdapter;
import com.example.groceriesmanager.Adapters.VideoAdapter;
import com.example.groceriesmanager.Models.Recipe;
import com.example.groceriesmanager.R;

import java.util.List;

public class YoutubeSearchFragment extends Fragment {
    EditText etYoutubeLookup;
    ImageButton ibYoutubeSearchClear;
    CheckBox checkboxVegan;
    CheckBox checkboxVegetarian;
    CheckBox checkboxGlutenFree;
    private static final String TAG = "YoutubeSearchFragment";
//    public static List<Recipe> recipeList;
    public VideoAdapter adapter;
    RecyclerView rvYoutubeSearch;
    private static final String QUERY_FILTER_VEGAN = "vegan";
    private static final String QUERY_FILTER_VEGETARIAN = "vegetarian";
    private static final String QUERY_FILTER_GLUTEN_FREE = "gluten-free";

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
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }
}

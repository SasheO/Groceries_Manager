package com.example.groceriesmanager.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceriesmanager.Activities.MainActivity;
import com.example.groceriesmanager.Adapters.FoodListAdapter;
import com.example.groceriesmanager.Activities.EditFoodItemActivity;
import com.example.groceriesmanager.Models.FoodItem;
import com.example.groceriesmanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PantryListFragment extends Fragment {
    RecyclerView rvPantryList;
    ImageButton btnAddPantryItem;
    FloatingActionButton fabtnSuggestRecipes;
    List<FoodItem> pantryList;
    private static final String TAG = "PantryListFragment";
    public FoodListAdapter adapter;
    private static final String type = "pantry";
    private MainActivity currentActivity;

    // required empty constructor
    public PantryListFragment() {}
    public PantryListFragment(MainActivity currentActivity) {this.currentActivity = currentActivity;}

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_pantry_list, parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        rvPantryList = (RecyclerView) view.findViewById(R.id.rvPantryList);
        btnAddPantryItem = view.findViewById(R.id.ibAddPantryItem);
        fabtnSuggestRecipes = view.findViewById(R.id.fabtnSuggestRecipes);
        pantryList = new ArrayList<>();
        queryPantryList();
        adapter = new FoodListAdapter(currentActivity, pantryList, type);
        // set the adapter on the recycler view
        rvPantryList.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvPantryList.setLayoutManager(new LinearLayoutManager(getActivity()));

        fabtnSuggestRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                suggestRecipes();
            }
        });

        btnAddPantryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pantryList.size() >= 30){
                    Toast.makeText(currentActivity, "Pantry list at maximum capacity. Delete old items to add new.", Toast.LENGTH_LONG).show();
                }
                else{
                    Intent intent = new Intent(currentActivity, EditFoodItemActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("process", "new");
                    startActivity(intent);
                }
            }
        });
    }

    class CategorizedFoods {
        public List<FoodItem> grains_legumes = new ArrayList<>();
        public List<FoodItem> protein = new ArrayList<>();
        public List<FoodItem> vegetables = new ArrayList<>();
        public List<FoodItem> canned_foods = new ArrayList<>();
        public List<FoodItem> other_category = new ArrayList<>();
    }

    private void suggestRecipes() {

        FragmentTransaction ft = currentActivity.getSupportFragmentManager().beginTransaction();
        String userQuery = "";

        // if there is nothing in the pantry list, let user know that there is nothing to search
        if (pantryList.size()==0){
            Toast.makeText(currentActivity, "nothing to search", Toast.LENGTH_SHORT).show();
            return;
        }

        // if the user did not manually select what recipe ingredients to search, do smart search
        if(adapter.selected.size() == 0){
        // check which food categories is which in pantry list
        // main for suggestions are grains/legumes, protein, veggies, canned food in that order
            CategorizedFoods foodCategorizer = new CategorizedFoods();
        for (FoodItem item: pantryList){
            if (item.getFoodCategory() == null){
                foodCategorizer.other_category.add(item);
                continue;
            }

            if (Objects.equals(item.getFoodCategory(), "fresh vegetables")){
                foodCategorizer.vegetables.add(item);
            }
            else if (Objects.equals(item.getFoodCategory(), "canned food")){
                foodCategorizer.canned_foods.add(item);
            }
            else if (Objects.equals(item.getFoodCategory(), "grains/legumes")){
                foodCategorizer.grains_legumes.add(item);
            }
            else if (Objects.equals(item.getFoodCategory(), "protein")){
                foodCategorizer.protein.add(item);
            }
            else{
                foodCategorizer.other_category.add(item);
            }
        }

        // todo: pick which will be searched for and implement edge case testing e.g. no veggies, only one veggie so the same result doesn't show up every time

        int index = (int)(Math.random() * pantryList.size());
        userQuery = pantryList.get(index).getName();
        }
        else{
            for (FoodItem foodItem: adapter.selected){
                userQuery = userQuery + foodItem.getName() + " ";
            }
        }
        RecipeSearchFragment fragmentDemo = RecipeSearchFragment.newInstance(userQuery);
        ft.replace(R.id.frameLayout, fragmentDemo);
        ft.commit();
    }

    private void queryPantryList() {
        // specify what type of data we want to query - Post.class
        ParseQuery<FoodItem> query = ParseQuery.getQuery(FoodItem.class);
        // include data where post is current post
        query.whereEqualTo("type", type);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        // necessary to include non-primitive types
        query.include("user");
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<FoodItem>() {
            @Override
            public void done(List<FoodItem> objects, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "error retrieving grocery list: " + e.toString());
                }
                else{
                    adapter.clear();
                    pantryList.addAll(objects);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}

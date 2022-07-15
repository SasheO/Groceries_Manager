package com.example.groceriesmanager.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PantryListFragment extends Fragment {
    RecyclerView rvPantryList;
    ImageButton btnAddPantryItem;
    FloatingActionButton fabtnSuggestRecipes;
    public List<FoodItem> pantryList;
    Switch switchSortAccordingToCategory;
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
        switchSortAccordingToCategory = view.findViewById(R.id.switchSortAccordingToCategory);
        pantryList = new ArrayList<>();
        queryPantryList();
        adapter = new FoodListAdapter(currentActivity, pantryList, type);
        // set the adapter on the recycler view
        rvPantryList.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvPantryList.setLayoutManager(new LinearLayoutManager(getActivity()));

        switchSortAccordingToCategory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    sortPantryAccordingToCategory();
                    adapter.notifyDataSetChanged();
                }
                else{
                    queryPantryList();
                }
            }
        });

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
                    editActivityResultLauncher.launch(intent);
                }
            }
        });
    }

    private void suggestRecipes() {

        FragmentTransaction ft = currentActivity.getSupportFragmentManager().beginTransaction();
        String userQuery = "";



        // if the user did not manually select what recipe ingredients to search, do smart search
        if(adapter.selected.size() == 0){

            // need at least two elements for smart search
            if (pantryList.size()<2){
                Toast.makeText(currentActivity, "not enough for random search", Toast.LENGTH_SHORT).show();
                return;
            }

        userQuery = getSmartSearchQuery();

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

    private String getSmartSearchQuery() {
        /*
        main for suggestions are grains/legumes, protein, veggies, canned food in that order
        order pantry list according to importance
        */
        String userQuery = "";

        if (!switchSortAccordingToCategory.isChecked()){
            sortPantryAccordingToCategory();
        }
        // get random element from first third which is likely to be a prioritized category
        int index = (int)(Math.random() * pantryList.size()/3);
        userQuery = pantryList.get(index).getName();
        pantryList.remove(index);
        index = (int)(Math.random() * pantryList.size());
        userQuery = userQuery + " " + pantryList.get(index).getName();
        return userQuery;
    }

    private void sortPantryAccordingToCategory(){
        // todo: populate
        /*
        (pantry list max size is 30,)
        this shuffles list (to somewhat randomize searches) then repeatedly moves items to the front to rearrange them in order of relevance when searching
*/
        List<FoodItem> organizedPantryList = new ArrayList<>(pantryList);
        Collections.shuffle(organizedPantryList);
        for (FoodItem item: pantryList){
            if (item.getFoodCategory()==null){
                organizedPantryList.remove(organizedPantryList.indexOf(item));
                organizedPantryList.add(0, item);
            }
        }
        // types is ordered in s
        List<String> types = new ArrayList<>(Arrays.asList("other", "beverages/dairy", "fresh fruits", "canned food", "fresh vegetables", "protein", "grains/legumes"));
        for (int i=0; i<types.size(); i++){
            for (FoodItem item: pantryList){
                if (item.getFoodCategory()!=null){
                    if (Objects.equals(item.getFoodCategory(), types.get(i))){
                        organizedPantryList.remove(organizedPantryList.indexOf(item));
                        organizedPantryList.add(0, item);
                    }
                }
            }
        }

        pantryList.clear();
        pantryList.addAll(organizedPantryList);
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
    public ActivityResultLauncher<Intent> editActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // If the user comes back to this activity from EditActivity
                    // with no error or cancellation
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // todo: Get the data passed from EditActivity
                        String process = data.getExtras().getString("process");
                        FoodItem foodItem = data.getParcelableExtra("fooditem");

                        if (Objects.equals(process, "new")){ // if creating new food item
                            pantryList.add(0, foodItem); // add it to recycler view
                            adapter.notifyDataSetChanged();
                        }
                        else { // if editing a food item
                            for (int i=0; i<pantryList.size(); i++){
                                if (foodItem.hasSameId(pantryList.get(i))){
                                    pantryList.set(i, foodItem); // update the food item in the recycler view
                                    adapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }
                    }
                }
            });
}

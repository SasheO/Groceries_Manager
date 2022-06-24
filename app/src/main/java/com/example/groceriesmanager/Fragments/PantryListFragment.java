package com.example.groceriesmanager.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceriesmanager.Adapters.FoodListAdapter;
import com.example.groceriesmanager.Activities.AddFoodItemActivity;
import com.example.groceriesmanager.Models.FoodItem;
import com.example.groceriesmanager.Models.User;
import com.example.groceriesmanager.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PantryListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    RecyclerView rvPantryList;
    ImageButton btnAddPantryItem;
    List<FoodItem> pantryList;
    private static final String TAG = "PantryListFragment";
    public FoodListAdapter adapter;
    private static final String type = "pantry";

    // required empty constructor
    public PantryListFragment() {}

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
        btnAddPantryItem = view.findViewById(R.id.btnAddPantryItem);
        pantryList = new ArrayList<>();
        User current_user = (User) ParseUser.getCurrentUser();
        pantryList = current_user.getPantryList();
        adapter = new FoodListAdapter(getContext(), pantryList, type);
        Log.i(TAG, "pantry list: " + pantryList.toString());

        // set the adapter on the recycler view
        rvPantryList.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvPantryList.setLayoutManager(new LinearLayoutManager(getActivity()));


        btnAddPantryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddFoodItemActivity.class);
                // todo: put extra that indicates that this is a new grocery list item
                intent.putExtra("type", type);
                startActivity(intent);
                //                addFoodItemActivityResultLauncher.launch(intent);
            }
        });
    }

//    ActivityResultLauncher<Intent> addFoodItemActivityResultLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    // If the user comes back to this activity from EditActivity
//                    // with no error or cancellation
//                    if (result.getResultCode() == Activity.RESULT_OK) {
//                        Intent data_passed_back = result.getData();
//                        // Get the data passed from EditActivity
//                        // String editedString = data.getExtras().getString("newString");
//                        // todo: get food item from grocery list and pass it in
//                        FoodItem newFoodItem = data_passed_back.getParcelableExtra("newFoodItem");
////                        groceryList.add(0, newFoodItem);
////                        adapter.notifyDataSetChanged();
//                    }
//                }
//            });
}

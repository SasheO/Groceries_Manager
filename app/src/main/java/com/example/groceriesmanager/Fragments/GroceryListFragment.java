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
import com.example.groceriesmanager.AddFoodItemActivity;
import com.example.groceriesmanager.Models.FoodItem;
import com.example.groceriesmanager.R;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class GroceryListFragment extends Fragment {
        // TODO: Rename parameter arguments, choose names that match
        RecyclerView rvGroceryList;
        ImageButton btnAddGroceryItem;
        List<FoodItem> groceryList;
        public static final String KEY_GROCERY_LIST = "groceryList";
        private static final String TAG = "GroceryListFragment";
        public FoodListAdapter adapter;

        // required empty constructor
        public GroceryListFragment() {}

        // The onCreateView method is called when Fragment should create its View object hierarchy,
        // either dynamically or via XML layout inflation.
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
            // Defines the xml file for the fragment
            return inflater.inflate(R.layout.fragment_grocery_list, parent, false);
        }

        // This event is triggered soon after onCreateView().
        // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
                // Setup any handles to view objects here
                rvGroceryList = (RecyclerView) view.findViewById(R.id.rvGroceryList);
                btnAddGroceryItem = view.findViewById(R.id.btnAddGroceryItem);
                groceryList = ParseUser.getCurrentUser().getList(KEY_GROCERY_LIST);
                groceryList = new ArrayList<>();
                Log.i(TAG, "grocery list: " + groceryList.toString());
                groceryList = ParseUser.getCurrentUser().getList(KEY_GROCERY_LIST);
                adapter = new FoodListAdapter(getContext(), groceryList);

                // set the adapter on the recycler view
                rvGroceryList.setAdapter(adapter);
                // set the layout manager on the recycler view
                rvGroceryList.setLayoutManager(new LinearLayoutManager(getActivity()));
                Log.i(TAG, "groceryList: "+groceryList.toString());
//                adapter.notifyDataSetChanged();

                btnAddGroceryItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                Intent intent = new Intent(getContext(), AddFoodItemActivity.class);
                                // todo: put extra that indicates that this is a new grocery list item
                                intent.putExtra("type", "grocery");
                                startActivity(intent);
                        }
                });
        }
}

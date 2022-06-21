package com.example.groceriesmanager.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceriesmanager.AddFoodItemActivity;
import com.example.groceriesmanager.R;

public class GroceryListFragment extends Fragment {
        // TODO: Rename parameter arguments, choose names that match
        RecyclerView rvGroceryList;
        ImageButton btnAddGroceryItem;


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
                rvGroceryList = view.findViewById(R.id.rvGroceryList);
                btnAddGroceryItem = view.findViewById(R.id.btnAddGroceryItem);


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

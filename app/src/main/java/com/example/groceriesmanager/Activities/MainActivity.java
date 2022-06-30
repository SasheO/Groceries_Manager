package com.example.groceriesmanager.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.groceriesmanager.Fragments.GroceryListFragment;
import com.example.groceriesmanager.Fragments.PantryListFragment;
import com.example.groceriesmanager.Fragments.RecipeSearchFragment;
import com.example.groceriesmanager.Fragments.UserProfileFragment;
import com.example.groceriesmanager.Fragments.YoutubeSearchFragment;
import com.example.groceriesmanager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    AnimatedBottomBar bottomNavigationView; // got AnimateBottomBar class from https://github.com/droppers/animatedbottombar
    // these lines below are necessary to be able to refer to the fragments from another fragment via the activity
    final FragmentManager fragmentManager = getSupportFragmentManager();
    public GroceryListFragment groceryListFragment = new GroceryListFragment();
    public PantryListFragment pantryListFragment = new PantryListFragment();
    public UserProfileFragment userProfileFragment = new UserProfileFragment();
    public RecipeSearchFragment recipeSearchFragment = new RecipeSearchFragment();
    public YoutubeSearchFragment youtubeSearchFragment = new YoutubeSearchFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // when you first open the main activity, the grocerylist fragment shows first
        fragmentManager.beginTransaction().replace(R.id.frameLayout, groceryListFragment).commit();

        bottomNavigationView.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            Fragment fragment;
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab selectedTab) {
                // switch to new tab
                if (selectedTab.getId() == R.id.miGroceryList){
                    fragment = groceryListFragment;
                }
                if (selectedTab.getId() == R.id.miPantryList){
                    fragment = pantryListFragment;
                }
                if (selectedTab.getId() == R.id.miRecipeSearch){
                    fragment = recipeSearchFragment;
                }
                if (selectedTab.getId() == R.id.miYoutubeSearch){
                    fragment = youtubeSearchFragment;
                }
                if (selectedTab.getId() == R.id.miUserProfile){
                    fragment = userProfileFragment;
                }
                fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });

    }


}
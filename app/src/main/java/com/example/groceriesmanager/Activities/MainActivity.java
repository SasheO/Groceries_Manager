package com.example.groceriesmanager.Activities;

import androidx.annotation.NonNull;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    BottomNavigationView bottomNavigationView;
    // these lines below are necessary to be able to refer to the fragments from another fragment via the activity
    final FragmentManager fragmentManager = getSupportFragmentManager();
    public GroceryListFragment groceryListFragment = new GroceryListFragment();
    public PantryListFragment pantryListFragment = new PantryListFragment();
    public UserProfileFragment savedRecipesFragment = new UserProfileFragment();
    public RecipeSearchFragment recipeSearchFragment = new RecipeSearchFragment();
    public YoutubeSearchFragment youtubeSearchFragment = new YoutubeSearchFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // when you first open the main activity, the grocerylist fragment shows first
        fragmentManager.beginTransaction().replace(R.id.frameLayout, groceryListFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                switch (item.getItemId()) {// this replaces the fragment housed in frameLayout with a feedfragment
                    case R.id.miPantryList:
                        // this replaces the fragment housed in frameLayout with a postfragment
                        fragment = pantryListFragment;
                        break;
                    case R.id.miUserProfile:
                        fragment = savedRecipesFragment;
                        break;
                    case R.id.miRecipeSearch:
                        fragment = recipeSearchFragment;
                        break;
                    case R.id.miYoutubeSearch:
                        fragment = youtubeSearchFragment;
                        break;
                    case R.id.miGroceryList:
                        fragment = groceryListFragment;
                        break;
                    default:
                        fragment = groceryListFragment;
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment).commit();
                return true;
            }
        });
    }


}
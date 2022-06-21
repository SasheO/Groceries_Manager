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
import com.example.groceriesmanager.Fragments.SavedRecipesFragment;
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
    public SavedRecipesFragment savedRecipesFragment = new SavedRecipesFragment();
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
                    case R.id.miSavedList:
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            // if user presses logout menu item button
            case R.id.miLogOut:
                ParseUser.logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e != null) {
                            Log.e(TAG, "Error signing out", e);
                            Toast.makeText(MainActivity.this, "Error signing out", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Log.i(TAG, "Sign out successful");
                        goToLoginActivity();
                        Toast.makeText(MainActivity.this, "Signed out", Toast.LENGTH_SHORT).show();
                    }
                    }
                );
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
}

    private void goToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
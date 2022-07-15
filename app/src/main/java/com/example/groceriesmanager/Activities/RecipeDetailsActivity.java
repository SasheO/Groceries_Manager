package com.example.groceriesmanager.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.groceriesmanager.Models.FoodItem;
import com.example.groceriesmanager.Models.Recipe;
import com.example.groceriesmanager.R;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class RecipeDetailsActivity extends AppCompatActivity {
    private static final String TAG = "RecipeDetailsActivity";
    TextView tvRecipeTitle;
    TextView tvLink;
    TextView tvRecipeFilters;
    TextView tvRecipeIngredientLines;
    TextView tvRecipeProcedureLines;
    TextView tvRecipeIngredientsLabel;
    TextView tvRecipeProcedureLabel;
    ImageButton ibSavedRecipeDelete;
    ImageButton ibSavedRecipeEdit;
    ImageButton ibExitRecipeDetails;
    Recipe recipe;
    String process;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        tvLink = findViewById(R.id.tvLink);
        tvRecipeTitle = findViewById(R.id.tvRecipeTitle);
        tvRecipeFilters = findViewById(R.id.tvRecipeFilters);
        tvRecipeIngredientLines = findViewById(R.id.tvRecipeIngredientLines2);
        tvRecipeProcedureLines = findViewById(R.id.tvRecipeProcedureLines);
        ibSavedRecipeEdit = findViewById(R.id.ibSavedRecipeEdit);
        ibSavedRecipeDelete = findViewById(R.id.ibSavedRecipeDelete);
        tvRecipeIngredientsLabel = findViewById(R.id.tvRecipeIngredientsLabel);
        tvRecipeProcedureLabel = findViewById(R.id.tvRecipeProcedureLabel);
        ibExitRecipeDetails = findViewById(R.id.ibExitRecipeDetails);

        recipe = getIntent().getParcelableExtra("recipe");

        populateActivity();

        ibExitRecipeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeActivity();
            }
        });

        ibSavedRecipeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process = "edit"; // will be passeed back into result listener when activity is closed
                Intent intent = new Intent(RecipeDetailsActivity.this, EditRecipeActivity.class);
                intent.putExtra("recipe", recipe);
                intent.putExtra("process", process);
                editActivityResultLauncher.launch(intent);
            }
        });

        ibSavedRecipeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipe.deleteInBackground();
                process = "delete"; // will be passeed back into result listener when activity is closed
                // todo: (optional) create snackbar that shows undo delete
                closeActivity();
            }
        });
    }

    private void closeActivity() {
        Intent data = new Intent();
        // Pass relevant data back as a result
        Log.i(TAG, "process: " + process);
        data.putExtra("process", process);
        data.putExtra("recipe", recipe);
        data.putExtra("type", "recipe");
        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish();
    }

    ActivityResultLauncher<Intent> editActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    // If the user comes back to this activity from EditActivity
                    // with no error or cancellation
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        // Get the data passed from EditRecipeActivity
                        recipe = data.getParcelableExtra("recipe");
                        populateActivity();
                    }
                }
            });

    private void populateActivity(){
        String link = recipe.getHyperlink_url();
        String title = recipe.getTitle();
        String filters = "";
        EnumSet<AccountSettingsActivity.dietFiltersEnum> recipeFilters = recipe.getFilters();
        String ingredients = "";
        List<FoodItem> recipeIngredients = recipe.getIngredients();
        String procedures = "";
        List<String> recipeProcedure = recipe.getProcedure();

        if (Objects.equals(recipe.getType(), "saved")) { // so you can only edit user recipes, not those saved from online
            ibSavedRecipeEdit.setVisibility(View.GONE);
        }

        tvRecipeTitle.setText(title);
        if (link!=null){
            tvLink.setText("Link: " + link);
        }
//        if (recipeFilters != null){
//            filters = recipeFilters.get(0);
//            for (int i=0; i<recipeFilters.size(); i++){
//                if (i==0){
//                    continue;
//                }
//                filters = filters + ", " + recipeFilters.get(i);
//            }
//            tvRecipeFilters.setText(filters);
//        }

        if (recipeIngredients != null){
            tvRecipeIngredientsLabel.setVisibility(View.VISIBLE);
            for (FoodItem ingredient: recipeIngredients){
                String quantity = ingredient.getQuantity();
                String measure = ingredient.getMeasure();
                String name = ingredient.getName();
                if (quantity!=null){
                    ingredients = ingredients + "\r\n" + quantity + " " + measure + " " + name;
                }
                else{
                    ingredients = ingredients + "\r\n" + name;
                }
            }
            tvRecipeIngredientLines.setText(ingredients);
        }

        if (recipeProcedure != null){
            tvRecipeProcedureLabel.setVisibility(View.VISIBLE);
            for (String procedure: recipeProcedure){
                procedures = procedures + "\r\n" + procedure;
            }
            tvRecipeProcedureLines.setText(procedures);
        }


    }
}
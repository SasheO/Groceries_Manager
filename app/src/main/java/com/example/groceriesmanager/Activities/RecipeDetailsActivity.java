package com.example.groceriesmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.groceriesmanager.Models.FoodItem;
import com.example.groceriesmanager.Models.Recipe;
import com.example.groceriesmanager.Models.Video;
import com.example.groceriesmanager.R;

import org.parceler.Parcels;

import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity {
    private static final String TAG = "RecipeDetailsActivity";
    TextView tvRecipeTitle;
    TextView tvLink;
    TextView tvRecipeFilters;
    TextView tvRecipeIngredientLines;
    TextView tvRecipeProcedureLines;
    ImageButton ibSavedRecipeDelete;
    ImageButton ibSavedRecipeEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        tvLink = findViewById(R.id.tvLink);
        tvRecipeTitle = findViewById(R.id.tvRecipeTitle);
        tvRecipeFilters = findViewById(R.id.tvRecipeFilters);
        tvRecipeIngredientLines = findViewById(R.id.tvRecipeIngredientLines);
        tvRecipeProcedureLines = findViewById(R.id.tvRecipeProcedureLines);
        ibSavedRecipeEdit = findViewById(R.id.ibSavedRecipeEdit);
        ibSavedRecipeDelete = findViewById(R.id.ibSavedRecipeDelete);

        Recipe recipe = getIntent().getParcelableExtra("recipe");

        String link = recipe.getHyperlink_url();
        String title = recipe.getTitle();
        String filters = "";
        List<String> recipeFilters = recipe.getFilters();
        String Ingredients;
        List<FoodItem> recipeIngredients = recipe.getIngredients();
        String Procedure;
        List<String> recipeProcedure = recipe.getProcedure();

        tvRecipeTitle.setText(title);
        if (link!=null){
            tvLink.setText("Link: " + link);
        }
        if (recipeFilters != null){
            filters = recipeFilters.get(0);
            for (int i=0; i<recipeFilters.size(); i++){
                if (i==0){
                    continue;
                }
                filters = filters + ", " + recipeFilters.get(i);
            }
            tvRecipeFilters.setText(filters);
        }

        if (recipeIngredients != null){
            // todo: populate
        }

        if (recipeProcedure != null){
            // todo: populate
        }

        ibSavedRecipeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: populate
            }
        });

        ibSavedRecipeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: populate
            }
        });
    }
}
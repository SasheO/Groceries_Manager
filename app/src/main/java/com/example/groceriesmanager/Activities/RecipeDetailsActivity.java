package com.example.groceriesmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.groceriesmanager.Models.FoodItem;
import com.example.groceriesmanager.Models.Recipe;
import com.example.groceriesmanager.Models.Video;
import com.example.groceriesmanager.R;

import org.parceler.Parcels;

import java.util.List;
import java.util.Objects;

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
            ingredients =  "Ingredients";
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
            procedures = "Steps";
            for (String procedure: recipeProcedure){
                    procedures = procedures + "\r\n" + procedure;
            }
            tvRecipeProcedureLines.setText(procedures);
        }

        ibSavedRecipeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(RecipeDetailsActivity.this, EditRecipeActivity.class);
                    intent.putExtra("recipe", recipe);
                    intent.putExtra("process", "edit");
                    startActivity(intent);
                    // todo: automatically update recipe details and user recipe adapter without refreshing
            }
        });

        ibSavedRecipeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipe.deleteInBackground();
                if(Objects.equals(recipe.getType(), "user")){ // remove from user recipe adapter
                    // todo: populate
                }
                else { // remove from saved recipe adapter
                    // todo: populate
                }
                finish();
            }
        });
    }
}
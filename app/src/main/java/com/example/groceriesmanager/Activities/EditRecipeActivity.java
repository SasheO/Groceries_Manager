package com.example.groceriesmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.groceriesmanager.Adapters.RecipeTextAdapter;
import com.example.groceriesmanager.Models.FoodItem;
import com.example.groceriesmanager.Models.Recipe;
import com.example.groceriesmanager.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EditRecipeActivity extends AppCompatActivity {
    private EditText etRecipeTitle;
    private EditText etLink;
    private CheckBox checkboxVegan;
    private CheckBox checkboxVegetarian;
    private CheckBox checkboxGlutenFree;
    private EditText etAddProcedure;
    private EditText etAddIngredient;
    private ImageButton ibAddIngredient;
    private ImageButton ibAddProcedure;
    private RecyclerView rvIngredients;
    private RecyclerView rvProcedure;
    private Button btnCancel;
    private Button btnSave;
    private Spinner spinnerIngredientMeasure;
    private EditText etIngredientQty;
    private boolean currentlyEditingIngredient = false;
    private FoodItem editedIngredient;
    private boolean currentlyEditingProcedure = false;
    private int editedProcedurePosition;
    Recipe userRecipe;
    String recipeTitle;
    String recipeLink;
    List<String> recipeProcedureList;
    List<String> recipeFiltersList;
    List<FoodItem> recipeIngredientList;
    public RecipeTextAdapter ingredientAdapter;
    public RecipeTextAdapter procedureAdapter;
    private static final String TAG = "EditRecipeActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        etRecipeTitle = findViewById(R.id.etRecipeTitle);
        etLink = findViewById(R.id.etLink);
        checkboxGlutenFree = findViewById(R.id.checkboxGlutenFree);
        checkboxVegan = findViewById(R.id.checkboxVegan);
        checkboxVegetarian = findViewById(R.id.checkboxVegetarian);
        ibAddIngredient = findViewById(R.id.ibAddIngredient);
        ibAddProcedure = findViewById(R.id.ibAddProcedure);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);
        rvIngredients = findViewById(R.id.rvIngredients);
        rvProcedure = findViewById(R.id.rvProcedure);
        etAddIngredient = findViewById(R.id.etAddIngredient);
        etAddProcedure = findViewById(R.id.etAddProcedure);
        etIngredientQty = findViewById(R.id.etIngredientQty);
        spinnerIngredientMeasure = findViewById(R.id.spinnerIngredientMeasure);

        recipeProcedureList = new ArrayList<>();
        recipeIngredientList = new ArrayList<>();
        recipeFiltersList = new ArrayList<>();

        // array adapter for rendering items into the ingredient measure spinner
        ArrayAdapter<CharSequence> foodMeasureAdapter = ArrayAdapter.createFromResource(this, R.array.food_measures, android.R.layout.simple_spinner_item);
        foodMeasureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerIngredientMeasure.setAdapter(foodMeasureAdapter);

        // recycler view adapter for ingredients
        ingredientAdapter = new RecipeTextAdapter("ingredient");
        ingredientAdapter.IngredientAdapter(EditRecipeActivity.this, recipeIngredientList);
        // set the adapter on the recycler view
        rvIngredients.setAdapter(ingredientAdapter);
        // set the layout manager on the recycler view
        rvIngredients.setLayoutManager(new LinearLayoutManager(EditRecipeActivity.this));

        // recycler view adapter for ingredients
        procedureAdapter = new RecipeTextAdapter("procedure");
        procedureAdapter.ProcedureAdapter(EditRecipeActivity.this, recipeProcedureList);
        // set the adapter on the recycler view
        rvProcedure.setAdapter(procedureAdapter);
        // set the layout manager on the recycler view
        rvProcedure.setLayoutManager(new LinearLayoutManager(EditRecipeActivity.this));

        String process = getIntent().getStringExtra("process");
        if (Objects.equals(process, "edit")){
            userRecipe = getIntent().getParcelableExtra("recipe");
            recipeTitle = userRecipe.getTitle();
            List<String> filters = userRecipe.getFilters();
            recipeLink = userRecipe.getHyperlink_url();
//            recipeIngredientListStr = userRecipe.getIngredientLines();

            etRecipeTitle.setText(recipeTitle);
            if (recipeLink != null){
                etLink.setText(recipeLink);
            }

            if (filters != null){
                if (filters.contains(getResources().getString(R.string.vegan))){
                    checkboxVegan.setChecked(true);
                }
                if (filters.contains(getResources().getString(R.string.vegetarian))){
                    checkboxVegetarian.setChecked(true);
                }
                if (filters.contains(getResources().getString(R.string.gluten_free))){
                    checkboxGlutenFree.setChecked(true);
                }
            }

            if (userRecipe.getProcedure() != null){
                recipeProcedureList.addAll(userRecipe.getProcedure());
                procedureAdapter.notifyDataSetChanged();
            }

            if (userRecipe.getIngredients() != null){
                recipeIngredientList.addAll(userRecipe.getIngredients());
                ingredientAdapter.notifyDataSetChanged();
            }

        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // todo: move the saving/editing recipe logic to a function for readability
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeTitle = etRecipeTitle.getText().toString().trim();
                if (Objects.equals(recipeTitle, "")){
                    Toast.makeText(EditRecipeActivity.this, "Type in a Recipe title.", Toast.LENGTH_LONG).show();
                }
                else {
                    if (Objects.equals(process, "new")) {
                    userRecipe = new Recipe();
                    userRecipe.setUser(ParseUser.getCurrentUser());
                    userRecipe.setType("user");
                }
                    userRecipe.setTitle(recipeTitle);

                    recipeLink = etLink.getText().toString().trim();
                    if (!Objects.equals(recipeLink, "")) {
                        userRecipe.setHyperlink_url(recipeLink);
                    }

                    if (checkboxVegetarian.isChecked()) {
                        recipeFiltersList.add(getResources().getString(R.string.vegetarian));
                    }
                    if (checkboxVegan.isChecked()) {
                        recipeFiltersList.add(getResources().getString(R.string.vegan));
                    }
                    if (checkboxGlutenFree.isChecked()) {
                        recipeFiltersList.add(getResources().getString(R.string.gluten_free));
                    }
                    if (recipeFiltersList.size() != 0) {
                        userRecipe.setFilters(recipeFiltersList);
                    }

                    if (recipeIngredientList.size() != 0) {
                        for (FoodItem ingredient : recipeIngredientList) {
                            ingredient.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        Log.e(TAG, "error saving ingredient: " + e.toString());
                                    }
                                }
                            });
                        }
                        userRecipe.setIngredients(recipeIngredientList);
                    }

                    if (recipeProcedureList.size() != 0) {
                        userRecipe.setProcedure(recipeProcedureList);
                    }

                    userRecipe.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Error saving user's recipe: " + e.toString());
                                Toast.makeText(EditRecipeActivity.this, "error saving recipe", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(EditRecipeActivity.this, "Recipe successfully saved", Toast.LENGTH_LONG).show();
                                // Prepare data intent
                                Intent data = new Intent();
                                // Pass relevant data back as a result
                                data.putExtra("recipe", userRecipe);
                                data.putExtra("process", process);
                                data.putExtra("type", "recipe");
                                // Activity finished ok, return the data
                                setResult(RESULT_OK, data); // set result code and bundle data for response
                                finish(); // closes the activity, pass data to parent
                            }
                        }
                    });


                }
            }
        });

        ibAddProcedure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String step = etAddProcedure.getText().toString().trim();
                if (!Objects.equals(step, "")) {

                    if (!currentlyEditingProcedure){
                        recipeProcedureList.add(step);
                    }
                    else{
                        recipeProcedureList.set(editedProcedurePosition, step);
                        currentlyEditingProcedure = false;
                    }
                    procedureAdapter.notifyDataSetChanged();
                    etAddProcedure.setText("");
                }
            }
        });

        ibAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredientName = etAddIngredient.getText().toString().trim();
                String ingredientMeasure = spinnerIngredientMeasure.getSelectedItem().toString();
                String ingredientQuantity = etIngredientQty.getText().toString().trim();

                if (!currentlyEditingIngredient){
                    editedIngredient = new FoodItem();
                    editedIngredient.setUser(ParseUser.getCurrentUser());
                    editedIngredient.setType("recipe");
                    recipeIngredientList.add(editedIngredient);
                }

                else{
                    currentlyEditingIngredient = false;
                }

                if (!Objects.equals(ingredientName, "")) {

                    if (!Objects.equals(ingredientQuantity, "")) {
                        editedIngredient.setMeasure(ingredientMeasure);
                        editedIngredient.setQuantity(ingredientQuantity);
                    }
                    etAddIngredient.setText("");
                    spinnerIngredientMeasure.setSelection(0);
                    editedIngredient.setName(ingredientName);
                    ingredientAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void editIngredient(FoodItem ingredient, int ingredientListPosition){
        if (!currentlyEditingIngredient){
            currentlyEditingIngredient = true;
            editedIngredient = recipeIngredientList.get(ingredientListPosition);
            String name = ingredient.getName();
            String quantity = ingredient.getQuantity();
            String measure = ingredient.getMeasure();
            etAddIngredient.setText(name);
            if (quantity != null){
                etIngredientQty.setText(quantity);
                int measure_position = Arrays.asList(getResources().getStringArray(R.array.food_measures)).indexOf(measure);
                spinnerIngredientMeasure.setSelection(measure_position);
            }
        }
        else{
            Toast.makeText(EditRecipeActivity.this, "already editing another ingredient", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteIngredient(FoodItem ingredient){
        if (!currentlyEditingIngredient){
            recipeIngredientList.remove(ingredient);
            ingredient.deleteFood();
            ingredientAdapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(EditRecipeActivity.this, "finish editing current ingredient before deleting", Toast.LENGTH_SHORT).show();
        }
    }

    public void editProcedure(int procedureListPosition){
        if (!currentlyEditingProcedure){
            currentlyEditingProcedure = true;
            editedProcedurePosition = procedureListPosition;
            etAddProcedure.setText(recipeProcedureList.get(editedProcedurePosition));
        }
        else{
            Toast.makeText(EditRecipeActivity.this, "already editing another procedure", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteProcedure(int procedureListPosition){
        if (!currentlyEditingProcedure){
            recipeProcedureList.remove(procedureListPosition);
            procedureAdapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(EditRecipeActivity.this, "finish editing current procedure before deleting", Toast.LENGTH_SHORT).show();
        }
    }
}
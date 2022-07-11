package com.example.groceriesmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.groceriesmanager.Adapters.IngredientAdapter;
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
    private ListView lvProcedure;
    private Button btnCancel;
    private Button btnSave;
    private Spinner spinnerIngredientMeasure;
    private EditText etIngredientQty;
    private boolean currentlyEditingIngredient = false;
    FoodItem ingredient_editing;
    Recipe userRecipe;
    String recipeTitle;
    String recipeLink;
    List<String> recipeIngredientListStr;
    List<String> recipeProcedureListStr;
    List<String> recipeFiltersList;
    List<FoodItem> recipeIngredientList;
    public IngredientAdapter ingredientAdapter;
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
        lvProcedure = findViewById(R.id.lvProcedure);
        etAddIngredient = findViewById(R.id.etAddIngredient);
        etAddProcedure = findViewById(R.id.etAddProcedure);
        etIngredientQty = findViewById(R.id.etIngredientQty);
        spinnerIngredientMeasure = findViewById(R.id.spinnerIngredientMeasure);

        recipeIngredientListStr = new ArrayList<>();
        recipeProcedureListStr = new ArrayList<>();
        recipeIngredientList = new ArrayList<>();
        recipeFiltersList = new ArrayList<>();

        // array adapter for rendering items into the ingredient measure spinner
        ArrayAdapter<CharSequence> foodMeasureAdapter = ArrayAdapter.createFromResource(this, R.array.food_measures, android.R.layout.simple_spinner_item);
        foodMeasureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerIngredientMeasure.setAdapter(foodMeasureAdapter);

        // array adapter for rendering items into procedure list
        ArrayAdapter<String> procedureAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, recipeProcedureListStr);
        lvProcedure.setAdapter(procedureAdapter);

        // recycler view adapter for ingredients
        ingredientAdapter = new IngredientAdapter(EditRecipeActivity.this, recipeIngredientList);
        // set the adapter on the recycler view
        rvIngredients.setAdapter(ingredientAdapter);
        // set the layout manager on the recycler view
        rvIngredients.setLayoutManager(new LinearLayoutManager(EditRecipeActivity.this));

        // todo: if process is "edit" from intent, populate the recipe details into the text view
        String process = getIntent().getStringExtra("process");
        if (Objects.equals(process, "edit")){
            userRecipe = getIntent().getParcelableExtra("recipe");
            recipeTitle = userRecipe.getTitle();
            List<String> filters = userRecipe.getFilters();
            recipeLink = userRecipe.getHyperlink_url();
            // todo: save ingredients, save ingredients as food item objects
//            recipeIngredientListStr = userRecipe.getIngredientLines();

            etRecipeTitle.setText(recipeTitle);
            if(recipeLink!=null){
                etLink.setText(recipeLink);
            }

            if (filters!=null){
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

            if (userRecipe.getProcedure()!=null){
                recipeProcedureListStr.addAll(userRecipe.getProcedure());
                procedureAdapter.notifyDataSetChanged();
            }

            if (recipeIngredientList!=null){
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

                    if (recipeIngredientListStr.size() != 0) {
                        userRecipe.setIngredientLines(recipeIngredientListStr);
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

                    if (recipeProcedureListStr.size() != 0) {
                        userRecipe.setProcedure(recipeProcedureListStr);
                    }

                    userRecipe.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Error saving user's recipe: " + e.toString());
                                Toast.makeText(EditRecipeActivity.this, "error saving recipe", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(EditRecipeActivity.this, "Recipe successfully saved", Toast.LENGTH_LONG).show();
                                finish();
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
                if (!Objects.equals(step, "")){
                    recipeProcedureListStr.add(step);
                    procedureAdapter.notifyDataSetChanged();
                    etAddProcedure.setText("");
                    // todo: make procedure editable and deletable
                }
            }
        });

        ibAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredientName = etAddIngredient.getText().toString().trim();
                String ingredientStr;
                String ingredientMeasure = spinnerIngredientMeasure.getSelectedItem().toString();
                String ingredientQuantity = etIngredientQty.getText().toString().trim();

                if(!currentlyEditingIngredient){

                    if (!Objects.equals(ingredientName, "")) {
                        ingredient_editing = new FoodItem();

                        if (!Objects.equals(ingredientQuantity, "")) {
                            ingredientStr = ingredientQuantity + " " + ingredientMeasure + " " + ingredientName;
                            ingredient_editing.setMeasure(ingredientMeasure);
                            ingredient_editing.setQuantity(ingredientQuantity);
                        } else {
                            ingredientStr = ingredientName;
                        }

                        recipeIngredientList.add(ingredient_editing);


                    }
            }
                else{
                    currentlyEditingIngredient = false;
                }
                etAddIngredient.setText("");
                spinnerIngredientMeasure.setSelection(0);
                ingredient_editing.setName(ingredientName);
                ingredient_editing.setUser(ParseUser.getCurrentUser());
                ingredient_editing.setType("recipe");
                ingredientAdapter.notifyDataSetChanged();

            }
        });
    }

    public void editIngredient(FoodItem ingredient, int ingredientListPosition){
        if(!currentlyEditingIngredient){
            currentlyEditingIngredient = true;
            ingredient_editing = recipeIngredientList.get(ingredientListPosition);
            String name = ingredient.getName();
            String quantity = ingredient.getQuantity();
            String measure = ingredient.getMeasure();
            etAddIngredient.setText(name);
            if (quantity!=null){
                etIngredientQty.setText(quantity);
                int measure_position = Arrays.asList(getResources().getStringArray(R.array.food_measures)).indexOf(measure);
                spinnerIngredientMeasure.setSelection(measure_position);
            }
        }
        else{
            Toast.makeText(EditRecipeActivity.this, "currently editing another ingredient, save it first!", Toast.LENGTH_LONG).show();
        }
//        ibAddIngredient.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = etAddIngredient.getText().toString();
//                String quantity = etIngredientQty.getText().toString();
//                String measure = spinnerIngredientMeasure.getSelectedItem().toString();
//
//                    try {
//                        if (ingredient.hasSameId(ingredient_editing)){
//                            ingredient_editing.setName(name);
//                            if (!Objects.equals(quantity, "")){
//                                ingredient_editing.setQuantity(quantity);
//                                ingredient_editing.setMeasure(measure);
//                            }
//                            else{
//                                ingredient_editing.setQuantity(null);
//                                ingredient_editing.setMeasure(null);
//                            }
//
//                            ingredientAdapter.notifyDataSetChanged();
//                            etIngredientQty.setText("");
//                            etAddIngredient.setText("");
//                            spinnerIngredientMeasure.setSelection(0);
//                        }
//                }
//                catch (Exception e){
//                        Log.e(TAG, "error editing ingredient: " + e.toString());
//                        Toast.makeText(EditRecipeActivity.this, "error editing ingredient", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                    return;
//            }
//        });
    }

    public void deleteIngredient(FoodItem ingredient){
        if (!currentlyEditingIngredient){
            recipeIngredientList.remove(ingredient);
            ingredient.deleteFood();
            ingredientAdapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(EditRecipeActivity.this, "finish editing current ingredient before deleting", Toast.LENGTH_LONG).show();
        }
    }
}
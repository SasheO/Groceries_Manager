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
import android.widget.TextView;
import android.widget.Toast;

import com.example.groceriesmanager.Adapters.RecipeTextAdapter;
import com.example.groceriesmanager.Models.FoodItem;
import com.example.groceriesmanager.Models.Recipe;
import com.example.groceriesmanager.R;
import com.google.android.flexbox.FlexboxLayout;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class EditRecipeActivity extends AppCompatActivity {
    private TextView tvTagLabel;
    private FlexboxLayout flexboxCheckboxes;
    private EditText etRecipeTitle;
    private EditText etLink;
    private CheckBox checkboxVegan;
    private CheckBox checkboxVegetarian;
    private CheckBox checkboxGlutenFree;
    private CheckBox checkboxDairyFree;
    private CheckBox checkboxAlcoholFree;
    private CheckBox checkboxImmunoSupportive;
    private CheckBox checkboxKetoFriendly;
    private CheckBox checkboxPescatarian;
    private CheckBox checkboxNoOilAdded;
    private CheckBox checkboxSoyFree;
    private CheckBox checkboxPeanutFree;
    private CheckBox checkboxKosher;
    private CheckBox checkboxPorkFree;
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
    private String process;
    private Recipe userRecipe;
    private String recipeTitle;
    private String recipeLink;
    private List<String> recipeProcedureList;
    private EnumSet<AccountSettingsActivity.dietFiltersEnum> recipeFiltersEnum;
    private List<FoodItem> recipeIngredientList;
    private static final String TAG = "EditRecipeActivity";
    public RecipeTextAdapter ingredientAdapter;
    public RecipeTextAdapter procedureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        flexboxCheckboxes = findViewById(R.id.flexboxCheckboxes);
        tvTagLabel = findViewById(R.id.tvTagLabel);
        etRecipeTitle = findViewById(R.id.etRecipeTitle);
        etLink = findViewById(R.id.etLink);
        checkboxGlutenFree = findViewById(R.id.checkboxGlutenFree);
        checkboxVegan = findViewById(R.id.checkboxVegan);
        checkboxVegetarian = findViewById(R.id.checkboxVegetarian);
        checkboxDairyFree = findViewById(R.id.checkboxDairyFree);
        checkboxAlcoholFree = findViewById(R.id.checkboxAlcoholFree);
        checkboxImmunoSupportive = findViewById(R.id.checkboxImmunoSupportive);
        checkboxKetoFriendly = findViewById(R.id.checkboxKetoFriendly);
        checkboxPescatarian = findViewById(R.id.checkboxPescatarian);
        checkboxNoOilAdded = findViewById(R.id.checkboxNoOilAdded);
        checkboxSoyFree = findViewById(R.id.checkboxSoyFree);
        checkboxPeanutFree = findViewById(R.id.checkboxPeanutFree);
        checkboxKosher = findViewById(R.id.checkboxKosher);
        checkboxPorkFree = findViewById(R.id.checkboxPorkFree);
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
        recipeFiltersEnum = EnumSet.noneOf(AccountSettingsActivity.dietFiltersEnum.class);

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

        process = getIntent().getStringExtra("process"); // will be either "new" or "edit" depending on whether user is creating new or editing already existing recipe
        if (Objects.equals(process, "edit")){
            userRecipe = getIntent().getParcelableExtra("recipe");
            recipeTitle = userRecipe.getTitle();
            recipeFiltersEnum = userRecipe.getFilters();
            recipeLink = userRecipe.getHyperlink_url();
//            recipeIngredientListStr = userRecipe.getIngredientLines();

            etRecipeTitle.setText(recipeTitle);
            if (recipeLink != null){
                etLink.setText(recipeLink);
            }


            // when user opens this page, they should see the right checkboxes checked
            setUserFilters(recipeFiltersEnum);

            if (userRecipe.getProcedure() != null){
                recipeProcedureList.addAll(userRecipe.getProcedure());
                procedureAdapter.notifyDataSetChanged();
            }

            if (userRecipe.getIngredients() != null){
                recipeIngredientList.addAll(userRecipe.getIngredients());
                ingredientAdapter.notifyDataSetChanged();
            }

        }

        tvTagLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flexboxCheckboxes.getVisibility()==View.GONE){
                    flexboxCheckboxes.setVisibility(View.VISIBLE);
                    tvTagLabel.setText("Close tags");
                }
                else {
                    flexboxCheckboxes.setVisibility(View.GONE);
                    tvTagLabel.setText("Edit tags");
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRecipe();
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

    // todo: find way to convert "checkbox" + enums to variable name and iterate through enum instead of manually typing out all the if statements
    private void setUserFilters(EnumSet<AccountSettingsActivity.dietFiltersEnum> userDietFilters){
        if (userDietFilters==null){ // if user has not chosen any filters
            return;
        }
        // if current user specified any of the following as a diet filter, set the checkbox upon opening the page
        if (userDietFilters.contains(AccountSettingsActivity.dietFiltersEnum.Vegan)){
            checkboxVegan.setChecked(true);
//            filters.add(dietFiltersEnum.Vegan);
        }
        if (userDietFilters.contains(AccountSettingsActivity.dietFiltersEnum.Vegetarian)){
            checkboxVegetarian.setChecked(true);
//            filters.add(dietFiltersEnum.Vegetarian);
        }
        if (userDietFilters.contains(AccountSettingsActivity.dietFiltersEnum.GlutenFree)){
            checkboxGlutenFree.setChecked(true);
//            filters.add(dietFiltersEnum.GlutenFree);
        }
        if (userDietFilters.contains(AccountSettingsActivity.dietFiltersEnum.DairyFree)){
            checkboxDairyFree.setChecked(true);
//            filters.add(dietFiltersEnum.DairyFree);
        }
        if (userDietFilters.contains(AccountSettingsActivity.dietFiltersEnum.AlcoholFree)){
            checkboxAlcoholFree.setChecked(true);
//            filters.add(dietFiltersEnum.AlcoholFree);
        }
        if (userDietFilters.contains(AccountSettingsActivity.dietFiltersEnum.ImmunoSupportive)){
            checkboxImmunoSupportive.setChecked(true);
//            filters.add(dietFiltersEnum.ImmunoSupportive);
        }
        if (userDietFilters.contains(AccountSettingsActivity.dietFiltersEnum.KetoFriendly)){
            checkboxKetoFriendly.setChecked(true);
//            filters.add(dietFiltersEnum.KetoFriendly);
        }
        if (userDietFilters.contains(AccountSettingsActivity.dietFiltersEnum.Pescatarian)){
            checkboxPescatarian.setChecked(true);
//            filters.add(dietFiltersEnum.Pescatarian);
        }
        if (userDietFilters.contains(AccountSettingsActivity.dietFiltersEnum.NoOilAdded)){
            checkboxNoOilAdded.setChecked(true);
//            filters.add(dietFiltersEnum.NoOilAdded);
        }
        if (userDietFilters.contains(AccountSettingsActivity.dietFiltersEnum.SoyFree)){
            checkboxSoyFree.setChecked(true);
//            filters.add(dietFiltersEnum.SoyFree);
        }
        if (userDietFilters.contains(AccountSettingsActivity.dietFiltersEnum.PeanutFree)){
            checkboxPeanutFree.setChecked(true);
//            filters.add(dietFiltersEnum.PeanutFree);
        }
        if (userDietFilters.contains(AccountSettingsActivity.dietFiltersEnum.Kosher)){
            checkboxKosher.setChecked(true);
//            filters.add(dietFiltersEnum.Kosher);
        }
        if (userDietFilters.contains(AccountSettingsActivity.dietFiltersEnum.PorkFree)){
            checkboxPorkFree.setChecked(true);
//            filters.add(dietFiltersEnum.PorkFree);
        }
    }

    // todo: find way to convert "checkbox" + enums to variable name and iterate through enum instead of manually typing out all the if else statements
    private void updateFiltersToSave() {
        if (checkboxGlutenFree.isChecked()){
            recipeFiltersEnum.add(AccountSettingsActivity.dietFiltersEnum.GlutenFree);
        }
        else {
            recipeFiltersEnum.remove(AccountSettingsActivity.dietFiltersEnum.GlutenFree);
        }
        if (checkboxVegetarian.isChecked()){
            recipeFiltersEnum.add(AccountSettingsActivity.dietFiltersEnum.Vegetarian);
        }
        else {
            recipeFiltersEnum.remove(AccountSettingsActivity.dietFiltersEnum.Vegetarian);
        }
        if (checkboxVegan.isChecked()){
            recipeFiltersEnum.add(AccountSettingsActivity.dietFiltersEnum.Vegan);
        }
        else {
            recipeFiltersEnum.remove(AccountSettingsActivity.dietFiltersEnum.Vegan);
        }

        if (checkboxDairyFree.isChecked()){
            recipeFiltersEnum.add(AccountSettingsActivity.dietFiltersEnum.DairyFree);
        }
        else {
            recipeFiltersEnum.remove(AccountSettingsActivity.dietFiltersEnum.DairyFree);
        }
        if (checkboxAlcoholFree.isChecked()){
            recipeFiltersEnum.add(AccountSettingsActivity.dietFiltersEnum.AlcoholFree);
        }
        else {
            recipeFiltersEnum.remove(AccountSettingsActivity.dietFiltersEnum.AlcoholFree);
        }
        if (checkboxImmunoSupportive.isChecked()){
            recipeFiltersEnum.add(AccountSettingsActivity.dietFiltersEnum.ImmunoSupportive);
        }
        else {
            recipeFiltersEnum.remove(AccountSettingsActivity.dietFiltersEnum.ImmunoSupportive);
        }
        if (checkboxKetoFriendly.isChecked()){
            recipeFiltersEnum.add(AccountSettingsActivity.dietFiltersEnum.KetoFriendly);
        }
        else {
            recipeFiltersEnum.remove(AccountSettingsActivity.dietFiltersEnum.KetoFriendly);
        }
        if (checkboxPescatarian.isChecked()){
            recipeFiltersEnum.add(AccountSettingsActivity.dietFiltersEnum.Pescatarian);
        }
        else {
            recipeFiltersEnum.remove(AccountSettingsActivity.dietFiltersEnum.Pescatarian);
        }
        if (checkboxNoOilAdded.isChecked()){
            recipeFiltersEnum.add(AccountSettingsActivity.dietFiltersEnum.NoOilAdded);
        }
        else {
            recipeFiltersEnum.remove(AccountSettingsActivity.dietFiltersEnum.NoOilAdded);
        }
        if (checkboxSoyFree.isChecked()){
            recipeFiltersEnum.add(AccountSettingsActivity.dietFiltersEnum.SoyFree);
        }
        else {
            recipeFiltersEnum.remove(AccountSettingsActivity.dietFiltersEnum.SoyFree);
        }
        if (checkboxPeanutFree.isChecked()){
            recipeFiltersEnum.add(AccountSettingsActivity.dietFiltersEnum.PeanutFree);
        }
        else {
            recipeFiltersEnum.remove(AccountSettingsActivity.dietFiltersEnum.PeanutFree);
        }
        if (checkboxKosher.isChecked()){
            recipeFiltersEnum.add(AccountSettingsActivity.dietFiltersEnum.Kosher);
        }
        else {
            recipeFiltersEnum.remove(AccountSettingsActivity.dietFiltersEnum.Kosher);
        }
        if (checkboxPorkFree.isChecked()){
            recipeFiltersEnum.add(AccountSettingsActivity.dietFiltersEnum.PorkFree);
        }
        else {
            recipeFiltersEnum.remove(AccountSettingsActivity.dietFiltersEnum.PorkFree);
        }
    }
    private void saveRecipe() {
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
                else{
                    userRecipe.remove(Recipe.KEY_HYPERLINK_URL);
                }

                // todo: save user filters
                updateFiltersToSave();
                userRecipe.setFilters(recipeFiltersEnum);

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

}
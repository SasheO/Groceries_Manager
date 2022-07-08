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
        ingredientAdapter = new IngredientAdapter(recipeIngredientList);
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
                // todo: populate adapter
                recipeProcedureListStr.addAll(userRecipe.getProcedure());
                procedureAdapter.notifyDataSetChanged();
            }

            if (recipeIngredientList!=null){
                // todo: populate adapter
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
                else{
                    Recipe newUserRecipe = new Recipe();
                    newUserRecipe.setUser(ParseUser.getCurrentUser());
                    newUserRecipe.setType("user");
                    newUserRecipe.setTitle(recipeTitle);

                    recipeLink = etLink.getText().toString().trim();
                    if (!Objects.equals(recipeLink, "")){
                        newUserRecipe.setHyperlink_url(recipeLink);
                    }

                    if (checkboxVegetarian.isChecked()){
                        recipeFiltersList.add(getResources().getString(R.string.vegetarian));
                    }
                    if (checkboxVegan.isChecked()){
                        recipeFiltersList.add(getResources().getString(R.string.vegan));
                    }
                    if (checkboxGlutenFree.isChecked()){
                        recipeFiltersList.add(getResources().getString(R.string.gluten_free));
                    }
                    if (recipeFiltersList.size()!=0){
                        newUserRecipe.setFilters(recipeFiltersList);
                    }

                    if (recipeIngredientListStr.size()!=0){
                        newUserRecipe.setIngredientLines(recipeIngredientListStr);
                        for (FoodItem ingredient: recipeIngredientList){
                            ingredient.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e!=null){
                                        Log.e(TAG, "error saving ingredient: " + e.toString());
                                    }
                                }
                            });
                        }
                        newUserRecipe.setIngredients(recipeIngredientList);
                    }

                    if (recipeProcedureListStr.size()!=0){
                        newUserRecipe.setProcedure(recipeProcedureListStr);
                    }

                    newUserRecipe.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e!=null){
                                Log.e(TAG, "Error saving user's recipe: " + e.toString());
                                Toast.makeText(EditRecipeActivity.this, "error saving recipe", Toast.LENGTH_LONG).show();
                            }
                            else{
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
                String ingredientName = etAddIngredient.getText().toString().trim();;
                String ingredientStr;
                String ingredientMeasure = spinnerIngredientMeasure.getSelectedItem().toString();
                String ingredientQuantity = etIngredientQty.getText().toString().trim();

                if (!Objects.equals(ingredientName, "")){
                    FoodItem ingredient = new FoodItem();

                    if (!Objects.equals(ingredientQuantity, "")){
                        ingredientStr = ingredientQuantity + " " + ingredientMeasure + " " + ingredientName;
                        ingredient.setMeasure(ingredientMeasure);
                        ingredient.setQuantity(ingredientQuantity);
                    }
                    else{
                        ingredientStr = ingredientName;
                    }

                    recipeIngredientListStr.add(ingredientStr);
                    etAddIngredient.setText("");
                    ingredient.setName(ingredientName);
                    ingredient.setUser(ParseUser.getCurrentUser());
                    ingredient.setType("recipe");
                    recipeIngredientList.add(ingredient);
                    ingredientAdapter.notifyDataSetChanged();

                }
            }
        });
    }
}
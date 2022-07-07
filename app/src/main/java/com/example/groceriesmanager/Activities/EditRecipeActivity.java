package com.example.groceriesmanager.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.groceriesmanager.Adapters.FoodListAdapter;
import com.example.groceriesmanager.Adapters.RecipeTextItemAdapter;
import com.example.groceriesmanager.Models.Recipe;
import com.example.groceriesmanager.Models.RecipeTextItem;
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
    private RecyclerView rvProcedure;
    private Button btnCancel;
    private Button btnSave;
    List<String> ingredientListStr;
    List<String> procedureListStr;
    List<String> filtersList;
    List<RecipeTextItem> ingredientList;
    List<RecipeTextItem> procedureList;
    public RecipeTextItemAdapter ingredientAdapter;
    public RecipeTextItemAdapter procedureAdapter;
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

        ingredientListStr = new ArrayList<>();
        procedureListStr = new ArrayList<>();
        filtersList = new ArrayList<>();

        //set recycler view adapters etc. here
        ingredientAdapter = new RecipeTextItemAdapter(ingredientList);
        procedureAdapter = new RecipeTextItemAdapter(procedureList);

        // set the adapter on the recycler view
        rvIngredients.setAdapter(ingredientAdapter);
        rvProcedure.setAdapter(procedureAdapter);
        // set the layout manager on the recycler view
        rvIngredients.setLayoutManager(new LinearLayoutManager(EditRecipeActivity.this));
        rvProcedure.setLayoutManager(new LinearLayoutManager(EditRecipeActivity.this));


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: check if required fields are filled then save in server
                String title = etRecipeTitle.getText().toString().trim();
                if (Objects.equals(title, "")){
                    Toast.makeText(EditRecipeActivity.this, "Type in a Recipe title.", Toast.LENGTH_LONG).show();
                }
                else{
                    Recipe newUserRecipe = new Recipe();
                    newUserRecipe.setUser(ParseUser.getCurrentUser());
                    newUserRecipe.setType("user");
                    newUserRecipe.setTitle(title);

                    String link = etLink.getText().toString().trim();
                    if (!Objects.equals(link, "")){
                        newUserRecipe.setHyperlink_url(link);
                    }

                    if (checkboxVegetarian.isChecked()){
                        filtersList.add("vegetarian");
                    }
                    if (checkboxVegan.isChecked()){
                        filtersList.add("vegan");
                    }
                    if (checkboxGlutenFree.isChecked()){
                        filtersList.add("gluten-free");
                    }
                    if (filtersList.size()!=0){
                        newUserRecipe.setFilters(filtersList);
                    }

                    if (ingredientListStr.size()!=0){
                        newUserRecipe.setIngredientLines(ingredientListStr);
                    }

                    if (procedureListStr.size()!=0){
                        newUserRecipe.setProcedure(procedureListStr);
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
                    procedureListStr.add(step);
                    etAddProcedure.setText("");
                    // todo: notify dataset changed when adapter is set
                }
            }
        });

        ibAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ingredient = etAddIngredient.getText().toString().trim();
                if (!Objects.equals(ingredient, "")){
                    ingredientListStr.add(ingredient);
                    etAddIngredient.setText("");
                    // todo: notify dataset changed when adapter is set
                }
            }
        });
    }
}
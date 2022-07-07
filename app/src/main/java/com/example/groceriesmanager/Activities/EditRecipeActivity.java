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
    List<String> ingredientListStr;
    List<String> procedureListStr;
    List<String> filtersList;
    List<FoodItem> ingredientList;
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

        ingredientListStr = new ArrayList<>();
        procedureListStr = new ArrayList<>();
        ingredientList = new ArrayList<>();
        filtersList = new ArrayList<>();

        // array adapter for rendering items into the ingredient measure spinner
        ArrayAdapter<CharSequence> foodMeasureAdapter = ArrayAdapter.createFromResource(this, R.array.food_measures, android.R.layout.simple_spinner_item);
        foodMeasureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerIngredientMeasure.setAdapter(foodMeasureAdapter);

        // array adapter for rendering items into procedure list
        ArrayAdapter<String> procedureAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, procedureListStr);
        lvProcedure.setAdapter(procedureAdapter);

        // recycler view adapter for ingredients
        ingredientAdapter = new IngredientAdapter(ingredientList);
        // set the adapter on the recycler view
        rvIngredients.setAdapter(ingredientAdapter);
        // set the layout manager on the recycler view
        rvIngredients.setLayoutManager(new LinearLayoutManager(EditRecipeActivity.this));


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                    ingredientListStr.add(ingredientStr);
                    etAddIngredient.setText("");
                    ingredient.setName(ingredientName);
                    ingredientList.add(ingredient);
                    ingredientAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
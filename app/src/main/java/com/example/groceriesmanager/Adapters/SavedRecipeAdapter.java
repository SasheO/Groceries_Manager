package com.example.groceriesmanager.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceriesmanager.Activities.EditRecipeActivity;
import com.example.groceriesmanager.Activities.MainActivity;
import com.example.groceriesmanager.Activities.RecipeDetailsActivity;
import com.example.groceriesmanager.Gestures.OnSwipeTouchListener;
import com.example.groceriesmanager.Models.FoodItem;
import com.example.groceriesmanager.Models.Recipe;
import com.example.groceriesmanager.R;
import com.parse.DeleteCallback;
import com.parse.ParseException;

import java.util.List;
import java.util.Objects;

public class SavedRecipeAdapter extends
        RecyclerView.Adapter<SavedRecipeAdapter.ViewHolder>{
    private List<Recipe> recipeList;
    private MainActivity context;
    private String type;
    private static final String TAG = "SavedRecipeAdapter";

    // constructor to set context
    public SavedRecipeAdapter(Context context, List<Recipe> recipeList, String type) {
        this.context = (MainActivity) context;
        this.recipeList = recipeList;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View recipeItemView = inflater.inflate(R.layout.item_saved_recipe, parent, false);

        // Return a new holder instance
        SavedRecipeAdapter.ViewHolder viewHolder = new ViewHolder(recipeItemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        holder.bind(recipe);

        holder.itemView.setClickable(true);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView tvRecipeTitle;
        private TextView tvOpenRecipeLink;
//        public TextView tvRecipeFilters;
        private ImageButton ibOpenRecipeLink;
        private RelativeLayout rlSavedRecipe;
        private ImageButton ibSavedRecipeDelete;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(this);
            tvRecipeTitle = itemView.findViewById(R.id.tvRecipeTitle);
            tvOpenRecipeLink = itemView.findViewById(R.id.tvOpenRecipeLink);
//            tvRecipeFilters = itemView.findViewById(R.id.tvRecipeFilters);
            ibOpenRecipeLink = itemView.findViewById(R.id.ibOpenRecipeLink);
            rlSavedRecipe = itemView.findViewById(R.id.rlSavedRecipe);
            ibSavedRecipeDelete = itemView.findViewById(R.id.ibSavedRecipeDelete);
        }

        public void bind(Recipe recipe) {
            tvRecipeTitle.setText(recipe.getTitle());


//            List<String> recipe_filters_array = recipe.getFilters();
//            if (recipe_filters_array!=null){
//                String recipe_filters = "Filters: " + recipe_filters_array.get(0);
//                for (int i=0; i<recipe_filters_array.size(); i++){
//                    if (i==0){
//                        continue;
//                    }
//                    recipe_filters = recipe_filters + ", " + recipe_filters_array.get(i);
//                }
//                tvRecipeFilters.setText(recipe_filters);
//            }

            if(recipe.getHyperlink_url() == null){
                tvOpenRecipeLink.setVisibility(View.GONE);
                ibOpenRecipeLink.setVisibility(View.GONE);
            }
            else {
                tvOpenRecipeLink.setVisibility(View.VISIBLE);
                ibOpenRecipeLink.setVisibility(View.VISIBLE);
            }

            ibSavedRecipeDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // delete all the food items/ingredients associated with recipe
                    if (recipe.getIngredients()!=null){
                        for (FoodItem ingredient: recipe.getIngredients()){
                            ingredient.deleteFood();
                        }
                    }

                    // delete recipe itself
                    recipe.deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e!=null){
                                Log.e(TAG, "error deleting recipe: " + e.toString());
                            }
                        }
                    });
                    recipeList.remove(recipe);
                    notifyDataSetChanged();
                }
            });

            tvOpenRecipeLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // lines below open hyperlink
                    Uri uri = Uri.parse(recipe.getHyperlink_url()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });

            ibOpenRecipeLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // lines below open hyperlink
                    Uri uri = Uri.parse(recipe.getHyperlink_url()); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }
            });

            rlSavedRecipe.setOnTouchListener(new OnSwipeTouchListener(context){
                @Override
                public void onClick() {
                    super.onClick();
                    // your on click here
                    Intent intent = new Intent(context, RecipeDetailsActivity.class);
                    intent.putExtra("recipe", recipe);
                    context.userProfileFragment.editActivityResultLauncher.launch(intent);
                }
                public void onLongClick(){
                    if (Objects.equals(type, "user")){ // so you can only edit user recipes, not those saved from online
                        Intent intent = new Intent(context, EditRecipeActivity.class);
                        intent.putExtra("recipe", recipe);
                        intent.putExtra("process", "edit");
                        context.userProfileFragment.editActivityResultLauncher.launch(intent);
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {}

    }

    public void clear() {
        recipeList.clear();
        notifyDataSetChanged();
    }
}

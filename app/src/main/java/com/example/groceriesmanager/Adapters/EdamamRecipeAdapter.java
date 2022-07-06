package com.example.groceriesmanager.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groceriesmanager.Activities.MainActivity;
import com.example.groceriesmanager.Gestures.OnSwipeTouchListener;
import com.example.groceriesmanager.Models.Recipe;
import com.example.groceriesmanager.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class EdamamRecipeAdapter extends
        RecyclerView.Adapter<EdamamRecipeAdapter.ViewHolder>{
    private List<Recipe> recipeList;
    MainActivity context;
    public static final String TAG = "RecipeAdapter";

    // constructor to set context
    public EdamamRecipeAdapter(Context context, List<Recipe> recipeList) {
        this.context = (MainActivity) context;
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View recipeItemView = inflater.inflate(R.layout.item_recipe_search, parent, false);

        // Return a new holder instance
        EdamamRecipeAdapter.ViewHolder viewHolder = new ViewHolder(recipeItemView);

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
        public TextView tvRecipeTitle;
        public TextView tvRecipeIngredientLines;
        public TextView tvOpenRecipeLink;
        public TextView tvRecipeFilters;
        public ImageButton ibOpenRecipeLink;
        public ImageView ivRecipeImage;
        public RelativeLayout rlRecipeSearch;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(this);
            tvRecipeTitle = (TextView) itemView.findViewById(R.id.tvRecipeTitle);
            tvRecipeIngredientLines = (TextView) itemView.findViewById(R.id.tvRecipeIngredientLines);
            tvOpenRecipeLink = (TextView) itemView.findViewById(R.id.tvOpenRecipeLink);
            tvRecipeFilters = (TextView) itemView.findViewById(R.id.tvRecipeFilters);
            ibOpenRecipeLink = (ImageButton) itemView.findViewById(R.id.ibOpenRecipeLink);
            ivRecipeImage = (ImageView) itemView.findViewById(R.id.ivRecipeImage);
            rlRecipeSearch = (RelativeLayout) itemView.findViewById(R.id.rlRecipeSearch);
        }

        public void bind(Recipe recipe) {
            tvRecipeTitle.setText(recipe.getTitle());
            // todo: set error and loading default images
            Glide.with(context)
                    .load(recipe.getImage_url())
                    .into(ivRecipeImage);

            // convert recipe lines from recipe from string array to a string that can be displayed in text box
            String recipe_ingredients = "INGREDIENTS";
            for (String ingredient: recipe.getIngredientLines()){
                recipe_ingredients = recipe_ingredients + "\r\n" + ingredient ;
            }
            tvRecipeIngredientLines.setText(recipe_ingredients);

            // convert filters from string array to a string that can be displayed in text box
            List<String> recipe_filters_array = recipe.getFilters();
            if (recipe_filters_array.size()!=0){
                String recipe_filters = "Filters: " + recipe_filters_array.get(0);
                for (int i=0; i<recipe_filters_array.size(); i++){
                    if (i==0){
                        continue;
                    }
                    recipe_filters = recipe_filters + ", " + recipe_filters_array.get(i);
                }
                tvRecipeFilters.setText(recipe_filters);
            }

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

            rlRecipeSearch.setOnTouchListener(new OnSwipeTouchListener(context) {
                @Override
                public void onClick() {
                    super.onClick();
                    // your on click here

                }
                public void onLongClick(){
                    // todo: set on long click



                    // inflate the layout of the popup window
//                    LayoutInflater inflater = LayoutInflater.from(context);
//                    View popupView = inflater.inflate(R.layout.popup_save_recipe, null);
//
//                    // create the popup window
//                    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
//                    int height = LinearLayout.LayoutParams.WRAP_CONTENT;
//                    boolean focusable = true; // lets taps outside the popup also dismiss it
//                    final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
//
//                    // show the popup window
//                    // which view you pass in doesn't matter, it is only used for the window tolken
//                    popupWindow.showAtLocation(itemView, Gravity.CENTER, 0, 0);
//
//                    // dismiss the popup window when touched
////                    popupView.setOnTouchListener(new View.OnTouchListener() {
////                        @Override
////                        public boolean onTouch(View v, MotionEvent event) {
////                            popupWindow.dismiss();
////                            return true;
////                        }
////                    });
                }
            });
        }

        @Override
        public void onClick(View v) {
            // todo: implement what happens when user clicks on a recipe
        }

    }

    public void clear() {
        recipeList.clear();
        notifyDataSetChanged();
    }
}

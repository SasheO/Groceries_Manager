package com.example.groceriesmanager.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.groceriesmanager.Activities.MainActivity;
import com.example.groceriesmanager.Models.Recipe;
import com.example.groceriesmanager.Models.Video;
import com.example.groceriesmanager.R;

import java.util.List;

public class VideoAdapter extends
        RecyclerView.Adapter<RecipeAdapter.ViewHolder>{
    private List<Video> videoList;
    MainActivity context;
    public static final String TAG = "VideoAdapter";

    public VideoAdapter(Context context, List<Video> videoList) {
        this.context = (MainActivity) context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        // todo: populate
//        public TextView tvRecipeTitle;
//        public TextView tvRecipeIngredientLines;
//        public TextView tvOpenRecipeLink;
//        public TextView tvRecipeFilters;
//        public ImageButton ibOpenRecipeLink;
//        public ImageView ivRecipeImage;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(this);
//            tvRecipeTitle = (TextView) itemView.findViewById(R.id.tvRecipeTitle);
//            tvRecipeIngredientLines = (TextView) itemView.findViewById(R.id.tvRecipeIngredientLines);
//            tvOpenRecipeLink = (TextView) itemView.findViewById(R.id.tvOpenRecipeLink);
//            tvRecipeFilters = (TextView) itemView.findViewById(R.id.tvRecipeFilters);
//            ibOpenRecipeLink = (ImageButton) itemView.findViewById(R.id.ibOpenRecipeLink);
//            ivRecipeImage = (ImageView) itemView.findViewById(R.id.ivRecipeImage);

            // todo: populate
        }

        public void bind(Video video) {
            // todo: populate
        }

        @Override
        public void onClick(View v) {
            // todo: implement what happens when user clicks on an item
        }

    }

    public void clear() {
        videoList.clear();
        notifyDataSetChanged();
    }
}

package com.example.groceriesmanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.groceriesmanager.R;

import java.util.List;

public class FoodCategorySpinnerAdapter extends ArrayAdapter<String> {

    Context context;
    List<String> foodCategoryList;

    public FoodCategorySpinnerAdapter(Context context, List<String> foodCategoryList){
        super(context, R.layout.my_selected_item, foodCategoryList);
        this.foodCategoryList = foodCategoryList;
        this.context = context;
    }
    // Override these methods and instead return our custom view (with image and text)
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_item_food_category, parent, false);

        TextView tvFoodCategory = row.findViewById(R.id.tvFoodCategory);
        ImageView ivFoodCategoryImage = row.findViewById(R.id.ivFoodCategoryImage);

        tvFoodCategory.setText(foodCategoryList.get(position));
         // todo: match food category pictures. check here for ideas https://www.youtube.com/watch?v=hdB9XOJu8rY
        Glide.with(context).load(context.getDrawable(  R.drawable.food_item_holder)).transform(new CircleCrop()).into(ivFoodCategoryImage);

        return row;
    }
}

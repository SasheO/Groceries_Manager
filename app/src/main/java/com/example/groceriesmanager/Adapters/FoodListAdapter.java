package com.example.groceriesmanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceriesmanager.Activities.MainActivity;
import com.example.groceriesmanager.Models.FoodItem;
import com.example.groceriesmanager.R;

import java.util.List;

public class FoodListAdapter extends
        RecyclerView.Adapter<FoodListAdapter.ViewHolder>{
    private List<FoodItem> foodItemList;
    MainActivity context;
    public static final String TAG = "FoodListAdapter";

    // constructor to set context
    public FoodListAdapter(Context context, List<FoodItem> foodItemList) {
        this.context = (MainActivity) context;
        this.foodItemList = foodItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View foodItemView = inflater.inflate(R.layout.item_food_list, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(foodItemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
//        Post post = postList.get(position);
        FoodItem foodItem = foodItemList.get(position);

        holder.bind(foodItem);

        holder.itemView.setClickable(true);
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvFoodItemName;
        public TextView tvFoodItemQty;
        public TextView tvFoodItemMeasure;
        public ImageView ivFoodItemPic;
        public ImageButton ibFoodItemCheckBox;
        public ImageButton ibFoodItemDelete;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(this);
            tvFoodItemName = (TextView) itemView.findViewById(R.id.tvFoodItemName);
            tvFoodItemQty = (TextView) itemView.findViewById(R.id.tvFoodItemQty);
            tvFoodItemMeasure = (TextView) itemView.findViewById(R.id.tvFoodItemMeasure);
            ivFoodItemPic = (ImageView) itemView.findViewById(R.id.ivFoodItemPic);
            ibFoodItemCheckBox = (ImageButton) itemView.findViewById(R.id.ibFoodItemCheckBox);
            ibFoodItemDelete = (ImageButton) itemView.findViewById(R.id.ibFoodItemDelete);
        }

        public void bind(FoodItem foodItem) {
            tvFoodItemName.setText(foodItem.getName());
            String qty = foodItem.getQuantity();
            if (qty == null){
                tvFoodItemQty.setVisibility(View.GONE);
                tvFoodItemMeasure.setVisibility(View.GONE);
            }
            else{
                tvFoodItemQty.setVisibility(View.VISIBLE);
                tvFoodItemQty.setText(foodItem.getQuantity());
                tvFoodItemMeasure.setText(foodItem.getMeasure());
            }
            // todo (stretch): glide the picture if there is one
            // todo: set on click listeners for the image buttons
        }

        @Override
        public void onClick(View v) {
            // todo: implement what happens when user clicks on an item
        }
    }
}

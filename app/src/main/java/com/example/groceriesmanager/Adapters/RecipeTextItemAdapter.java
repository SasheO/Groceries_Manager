package com.example.groceriesmanager.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceriesmanager.Models.Recipe;
import com.example.groceriesmanager.Models.RecipeTextItem;
import com.example.groceriesmanager.R;

import java.util.List;

public class RecipeTextItemAdapter extends RecyclerView.Adapter<RecipeTextItemAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private List<RecipeTextItem> itemList;

    // Pass in the contact array into the constructor
    public RecipeTextItemAdapter(List<RecipeTextItem> items) {
        itemList = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View itemView = inflater.inflate(R.layout.item_recipe_text, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeTextItem item = itemList.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvText;
        public ImageButton ibDelete;
        public ImageButton ibEdit;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            itemView.setOnClickListener(this);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
            ibDelete = (ImageButton) itemView.findViewById(R.id.ibDelete);
            ibEdit = (ImageButton) itemView.findViewById(R.id.ibEdit);
        }

        @Override
        public void onClick(View v) {
            // todo: edit using function
        }

        public void bind(RecipeTextItem item) {
            tvText.setText(item.text);

            // todo: set click listener for delete and edit buttons
            ibEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // todo: edit using function
                }
            });

            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemList.remove(item);
                    notifyDataSetChanged();
                }
            });
        }
    }


}
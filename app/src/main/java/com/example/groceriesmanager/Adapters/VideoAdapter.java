package com.example.groceriesmanager.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
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
        RecyclerView.Adapter<VideoAdapter.ViewHolder>{
    private List<Video> videoList;
    MainActivity context;
    public static final String TAG = "VideoAdapter";

    public VideoAdapter(Context context, List<Video> videoList) {
        this.context = (MainActivity) context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View videoItemView = inflater.inflate(R.layout.item_video_search, parent, false);

        // Return a new holder instance
        VideoAdapter.ViewHolder viewHolder = new VideoAdapter.ViewHolder(videoItemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Video video = videoList.get(position);

        holder.bind(video);

        holder.itemView.setClickable(true);
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
        public TextView tvVideoeTitle;
        public ImageView ivVideoThumbnail;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(this);
            tvVideoeTitle = (TextView) itemView.findViewById(R.id.tvVideoTitle);
            ivVideoThumbnail = (ImageView) itemView.findViewById(R.id.ivVideoThumbnail);

        }

        public void bind(Video video) {
            tvVideoeTitle.setText(video.getTitle());
            // todo: set error and loading default images
            Glide.with(context)
                    .load(video.getThumbnail_url())
                    .into(ivVideoThumbnail);
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

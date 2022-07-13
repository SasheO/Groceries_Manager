package com.example.groceriesmanager.Activities;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.groceriesmanager.Models.Video;
import com.example.groceriesmanager.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.parceler.Parcels;

public class VideoPlayerActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Video video = Parcels.unwrap(getIntent().getParcelableExtra("video"));
        ImageButton ibExitPlayer = (ImageButton) findViewById(R.id.ibExitPlayer);
        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.ytPlayer);
        TextView tvVideoTitle = (TextView) findViewById(R.id.tvVideoTitle);
        TextView tvVideoDescription = (TextView) findViewById(R.id.tvVideoDescription);
        TextView tvChannelTitle = (TextView) findViewById(R.id.tvChannelTitle);

        tvVideoTitle.setText(video.getTitle());
        tvChannelTitle.setText(video.getChannelTitle());
        tvVideoDescription.setText(video.getDescription());

        youTubePlayerView.initialize(getResources().getString(R.string.youtube_api_key),
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.cueVideo(video.getVideoID());
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });

        ibExitPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
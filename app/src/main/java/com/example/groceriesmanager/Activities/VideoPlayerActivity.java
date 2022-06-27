package com.example.groceriesmanager.Activities;


import android.os.Bundle;

import com.example.groceriesmanager.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class VideoPlayerActivity extends YouTubeBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        YouTubePlayerView youTubePlayerView = (YouTubePlayerView) findViewById(R.id.ytPlayer);

        youTubePlayerView.initialize(getResources().getString(R.string.youtube_api_key),
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                        youTubePlayer.cueVideo("qtzjL2gIqRo"); // todo: get video id from intent
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });
    }
}
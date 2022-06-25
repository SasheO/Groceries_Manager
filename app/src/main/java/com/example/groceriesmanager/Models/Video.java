package com.example.groceriesmanager.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Video {
    private String thumbnail_url;
    private String title;
    private String channel;
    private String videoID;

    // required empty constructor for Parcel library
    public Video(){}

    public Video(JSONObject jsonObject) throws JSONException {
        // todo: get the required stuff from the JSONobject
        this.thumbnail_url = jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url");
        this.title = jsonObject.getJSONObject("snippet").getString("title");
        this.channel = jsonObject.getJSONObject("snippet").getString("channelTitle");
        this.videoID = jsonObject.getJSONObject("id").getString("videoId");
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public static List<Video> fromJsonArray(JSONArray videoJsonArray) throws JSONException {
        List<Video> videoList = new ArrayList<>();
        for (int i=0; i<videoJsonArray.length(); i++){
            videoList.add(new Video(videoJsonArray.getJSONObject(i)));
        }
        return videoList;

    }

}

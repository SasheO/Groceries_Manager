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
    private String channelTitle;
    private String videoID;
    private String description;

    // required empty constructor for Parcel library
    public Video(){}

    public Video(JSONObject jsonObject) throws JSONException {
        // todo: get the required stuff from the JSONobject
        this.thumbnail_url = jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url");
        this.title = jsonObject.getJSONObject("snippet").getString("title");
        this.channelTitle = jsonObject.getJSONObject("snippet").getString("channelTitle");
        this.videoID = jsonObject.getJSONObject("id").getString("videoId");
        this.description = jsonObject.getJSONObject("snippet").getString("description");
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

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getDescription() { return description;}

    public void setDescription(String description) {this.description = description;}


    public static List<Video> fromJsonArray(JSONArray videoJsonArray) throws JSONException {
        List<Video> videoList = new ArrayList<>();
        for (int i=0; i<videoJsonArray.length(); i++){
            videoList.add(new Video(videoJsonArray.getJSONObject(i)));
        }
        return videoList;

    }

}

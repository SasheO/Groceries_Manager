package com.example.groceriesmanager.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("Video")
public class Video extends ParseObject {
    private static final String TAG = "Video";
    private static final String KEY_TITLE = "title";
    private static final String KEY_VIDEO_ID = "videoID";
    private static final String KEY_CHANNEL_TITLE = "channelTitle";
    private static final String KEY_THUMBNAIL_URL = "thumbnailURL";
    private static final String KEY_USER = "user";
    private String description;

    // required empty constructor for Parcel library
    public Video(){}

    public Video(JSONObject jsonObject) throws JSONException {
        put(KEY_THUMBNAIL_URL, jsonObject.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("medium").getString("url"));
        put(KEY_TITLE, jsonObject.getJSONObject("snippet").getString("title"));
        put(KEY_CHANNEL_TITLE, jsonObject.getJSONObject("snippet").getString("channelTitle"));
        put(KEY_VIDEO_ID, jsonObject.getJSONObject("id").getString("videoId"));
        this.description = jsonObject.getJSONObject("snippet").getString("description");
    }

    public String getThumbnail_url() {
        return getString(KEY_THUMBNAIL_URL);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public String getChannelTitle() {
        return getString(KEY_CHANNEL_TITLE);
    }

    public String getVideoID() {
        return getString(KEY_VIDEO_ID);
    }

    public String getDescription() { return description;}

    public ParseUser getUser(){return getParseUser(KEY_USER);}

    public void setUser(ParseUser user){
        put(KEY_USER, user);
    }

    public static List<Video> fromJsonArray(JSONArray videoJsonArray) throws JSONException {
        List<Video> videoList = new ArrayList<>();
        for (int i=0; i<videoJsonArray.length(); i++){
            videoList.add(new Video(videoJsonArray.getJSONObject(i)));
        }
        return videoList;

    }

}

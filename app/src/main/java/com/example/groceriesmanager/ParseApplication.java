package com.example.groceriesmanager;

import android.app.Application;

import com.example.groceriesmanager.Models.FoodItem;
import com.example.groceriesmanager.Models.User;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // register all classes that inherit parse
        ParseObject.registerSubclass(FoodItem.class);
        ParseObject.registerSubclass(User.class);

        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // any network interceptors must be added with the Configuration Builder given this syntax
        builder.networkInterceptors().add(httpLoggingInterceptor);



        // Set applicationId and server based on the values in the Back4App settings.
        // todo: set client key and server
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.application_id))
                .clientKey(getString(R.string.client_key))
                .clientBuilder(builder)
                .server("https://parseapi.back4app.com").build());
    }
}

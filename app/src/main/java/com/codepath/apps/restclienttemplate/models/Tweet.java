package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Tweet {
    public String body;
    public String createdAt;
    public User user;
    public long id;
    public int favNum;
    public boolean fav;
    public int retweetNum;
    public boolean retweet;


    //empty constructor for Parcel
    public  Tweet(){}

    public static Tweet getTweetFromJson(JSONObject json) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = json.getString("text");
        tweet.createdAt = json.getString("created_at");
        tweet.user = User.getUserFromJson(json.getJSONObject("user"));
        tweet.id = json.getLong("id");
        tweet.favNum = json.getInt("favorite_count");
        tweet.fav = json.getBoolean("favorited");
        tweet.retweetNum = json.getInt("retweet_count");
        tweet.retweet = json.getBoolean("retweeted");
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> list = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            list.add(getTweetFromJson(jsonArray.getJSONObject(i)));
        }
        return list;
    }
}

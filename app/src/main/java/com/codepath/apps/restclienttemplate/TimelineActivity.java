package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    TwitterClient client;
    String TAG = "TimelineActivity";
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        //find the recycler view
        rvTweets = findViewById(R.id.rvTweets);

        //init the list of tweets and adapter
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);

        //recycler view set up: layout manager and the adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(adapter);

        populateHomeTimeline();
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG,"Success" + json.toString());
                JSONArray jsonArray =  json.jsonArray;
                try {
                    tweets.addAll(fromJsonArray(jsonArray));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG,"Json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG,"Fail", throwable);
            }
        });
    }

    public Tweet getTweetFromJson(JSONObject json) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = json.getString("text");
        tweet.createdAt = json.getString("created_at");
        tweet.user = getUserFromJson(json.getJSONObject("user"));
        return tweet;
    }

    public User getUserFromJson(JSONObject json) throws JSONException {
        User user = new User();
        user.name = json.getString("name");
        user.screenName = json.getString("screen_name");
        user.imageURL = json.getString("profile_image_url_https");
        return user;
    }

    public List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> list = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); i++) {
            list.add(getTweetFromJson(jsonArray.getJSONObject(i)));
        }
        return list;
    }
}
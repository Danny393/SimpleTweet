package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    Context context;
    List<Tweet> tweets;
    TwitterClient client;

    //Pass in context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
        client = TwitterApp.getRestClient(context);
    }

    //for each row, inflate the layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    //bind values based on the position of element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public void clear() {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Tweet> list) {
        tweets.addAll(list);
        notifyDataSetChanged();
    }

    //Define viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfile;
        TextView tvUsername;
        TextView tvBody;
        TextView tvTime;
        Button btnHeart;
        Button btnRetweet;
        TextView tvNumHeart;
        TextView tvNumRetweet;

        public  ViewHolder(@NotNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfilePic);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvUsername = itemView.findViewById(R.id.tvDisplayName);
            tvTime = itemView.findViewById(R.id.tvTimestamp);
            btnHeart = itemView.findViewById(R.id.btnHeart);
            btnRetweet = itemView.findViewById(R.id.btnRetweet);
            tvNumHeart = itemView.findViewById(R.id.tvHeartNum);
            tvNumRetweet = itemView.findViewById(R.id.tvRetweetNum);
        }

        public void bind(final Tweet tweet) {
            tvBody.setText(tweet.body);
            tvUsername.setText(tweet.user.screenName);
            Glide.with(context).load(tweet.user.imageURL).circleCrop().into(ivProfile);
            tvTime.setText(TimeFormatter.getTimeDifference(tweet.createdAt));

            if(tweet.favNum == 0) tvNumHeart.setText("");
            else tvNumHeart.setText(String.valueOf(tweet.favNum));

            if(tweet.retweetNum == 0) tvNumRetweet.setText("");
            else tvNumRetweet.setText(String.valueOf(tweet.retweetNum));

            if(tweet.fav) btnHeart.setSelected(true);
            if(tweet.retweet) btnRetweet.setSelected(true);

            btnHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view.isSelected()){
                        view.setSelected(false);
                        if(tvNumHeart.getText().toString().equals("1")) tvNumHeart.setText("");
                        else tvNumHeart.setText(String.valueOf(Integer.parseInt(tvNumHeart.getText().toString()) - 1));

                        client.unfavoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i("TweetButton","Success to unfav");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("TweetButton","Failure to unfav");
                            }
                        });
                    }
                    else{
                        view.setSelected(true);
                        if(tvNumHeart.getText().toString().equals("")) tvNumHeart.setText("1");
                        else tvNumHeart.setText(String.valueOf(Integer.parseInt(tvNumHeart.getText().toString()) + 1));

                        client.favoriteTweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i("TweetButton","Success to fav");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("TweetButton","Failure to fav");
                            }
                        });
                    }
                }
            });
            btnRetweet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(view.isSelected()){
                        view.setSelected(false);
                        if(tvNumRetweet.getText().toString().equals("1")) tvNumRetweet.setText("");
                        else tvNumRetweet.setText(String.valueOf(Integer.parseInt(tvNumRetweet.getText().toString()) - 1));

                        client.unretweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i("TweetButton","Success to unretweet");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("TweetButton","Failure to unretweet");
                            }
                        });
                    }
                    else{
                        view.setSelected(true);
                        if(tvNumRetweet.getText().toString().equals("")) tvNumRetweet.setText("1");
                        else tvNumRetweet.setText(String.valueOf(Integer.parseInt(tvNumRetweet.getText().toString()) + 1));

                        client.retweet(tweet.id, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i("TweetButton","Success to retweet");
                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e("TweetButton","Failure to retweet");
                            }
                        });
                    }
                }
            });
        }
    }
}

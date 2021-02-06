package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder>{

    Context context;
    List<Tweet> tweets;

    //Pass in context and list of tweets
    public TweetsAdapter(Context context, List<Tweet> tweets) {
        this.context = context;
        this.tweets = tweets;
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

    //Define viewholder
    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProfile;
        TextView tvUsername;
        TextView tvBody;

        public  ViewHolder(@NotNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfilePic);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvUsername = itemView.findViewById(R.id.tvDisplayName);
        }

        public void bind(Tweet tweet) {
            tvBody.setText(tweet.body);
            tvUsername.setText(tweet.user.screenName);
            Glide.with(context).load(tweet.user.imageURL).into(ivProfile);
        }
    }
}

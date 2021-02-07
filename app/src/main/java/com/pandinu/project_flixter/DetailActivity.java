package com.pandinu.project_flixter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.method.MovementMethod;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.pandinu.project_flixter.adapters.CommentAdapter;
import com.pandinu.project_flixter.models.Comment;
import com.pandinu.project_flixter.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {
    private static final String YOUTUBE_API_KEY = "AIzaSyC5ff6e4blZRWqk8tRjALIty1hYf23IlFo";
    private static final String VIDEOS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private static final String COMMENT_URL =
            "https://www.googleapis.com/youtube/v3/commentThreads?key=AIzaSyC5ff6e4blZRWqk8tRjALIty1hYf23IlFo&textFormat=plainText&part=snippet&videoId=%s&maxResults=50";
    TextView tvTitle, tvOverview;
    RatingBar tvRatingBar;
    YouTubePlayerView youTubePlayerView;

    CommentAdapter commentAdapter;

    List<Comment> videoComments;

    RecyclerView tvCommentSection;

    private String tempYoutubeKey="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        videoComments = new ArrayList<>();

        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.detail_title_textview);
        tvOverview = findViewById(R.id.detail_tvOverview);
        tvRatingBar = findViewById(R.id.detail_tvRatingBar);
        youTubePlayerView = findViewById(R.id.player);
        tvCommentSection = findViewById(R.id.detail_tvComments);

        commentAdapter = new CommentAdapter(this, videoComments);

        tvCommentSection.setAdapter(commentAdapter);

        tvCommentSection.setLayoutManager(new LinearLayoutManager(this));




        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        //String title = getIntent().getStringExtra("title");
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvRatingBar.setRating((float) movie.getRating());

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(VIDEOS_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if(results.length() == 0){
                        return;
                    }else{
                        String youtubeKey = results.getJSONObject(0).getString("key");
                        Log.e("DetailActivity", youtubeKey);
                        initializeYoutube(youtubeKey);
                        getComments(youtubeKey);
                    }

                } catch (JSONException e) {
                    Log.e("DetailActivity", "API Error");
                    e.printStackTrace();
                }
            }

            private void getComments(String youtubeKey) {
                //client.get(String.format(COMMENT_URL, movie.getMovieId()), new JsonHttpResponseHandler() {
                client.get(String.format(COMMENT_URL, youtubeKey), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Headers headers, JSON json) {
                        try {
                            JSONArray results = json.jsonObject.getJSONArray("items");

                            Log.i("comment_result", "Array has been read");

                            for(int j = 0; j< results.length(); j++){
                                String comment = results.getJSONObject(j)
                                        .getJSONObject("snippet")
                                        .getJSONObject("topLevelComment")
                                        .getJSONObject("snippet")
                                        .getString("textOriginal");
                                String author = results.getJSONObject(j)
                                        .getJSONObject("snippet")
                                        .getJSONObject("topLevelComment")
                                        .getJSONObject("snippet")
                                        .getString("authorDisplayName");

                                Comment newComment = new Comment(author, comment);

                                videoComments.add(newComment);
                                Log.e("Comment_added", newComment.getAuthor()+ "'s comment has been added to list" );

                                Log.i("comment_result",
                                        results.getJSONObject(j)
                                                .getJSONObject("snippet")
                                                .getJSONObject("topLevelComment")
                                                .getJSONObject("snippet")
                                                .getString("textOriginal")
                                );
                            }

                            commentAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("comment_result", "exception");
                        }
                    }

                    @Override
                    public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                        Log.e("comment_result", "error");
                    }
                });
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {

            }
        });


    }

    private void initializeYoutube(String youtubeKey) {
        youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.cueVideo(youtubeKey);
            }
            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }
}
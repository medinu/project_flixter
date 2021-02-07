package com.pandinu.project_flixter.adapters;

import android.app.MediaRouteButton;
import android.content.Context;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pandinu.project_flixter.DetailActivity;
import com.pandinu.project_flixter.R;
import com.pandinu.project_flixter.models.Movie;

import org.parceler.Parcels;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // Involves population data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.bind(movie);
    }

    // Returen total count of items in list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout mContainer;
        TextView mTvTitle;
        TextView mTvOverview;
        ImageView mIvPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tvTitle);
            mTvOverview = itemView.findViewById(R.id.tvOverview);
            mIvPoster = itemView.findViewById(R.id.ivPoster);
            mContainer = itemView.findViewById(R.id.rvMovie);
        }

        public void bind(Movie movie) {
            mTvTitle.setText(movie.getTitle());
            mTvOverview.setText(movie.getOverview());

            String imgUrl = movie.getPosterPath();

            // Check orientation
            if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ){
                imgUrl = movie.getBackdropPath();
            }

            Glide.with(context)
                    .load(imgUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(mIvPoster);

            // register click listener on the whole containler
            // navigate to a new activity on tap

            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,  DetailActivity.class);
                    i.putExtra("movie", Parcels.wrap(movie));
                    context.startActivity(i);
                    //Toast.makeText(context, movie.getTitle(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

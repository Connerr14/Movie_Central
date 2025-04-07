package com.example.movie_central.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_central.R;
import com.example.movie_central.model.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class FavouritesAdapter extends RecyclerView.Adapter<FavouriteViewHolder> {
    private List<Movie> favouriteMovies;
    private FavouriteClickListener clickListener;

    public FavouritesAdapter(FavouriteClickListener listener) {
        this.favouriteMovies = new ArrayList<>();
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favourite_movie, parent, false);
        return new FavouriteViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        Movie movie = favouriteMovies.get(position);

        // Set movie data
        holder.titleTextView.setText(movie.getTitle());
        holder.studioTextView.setText(movie.getGenre());
        holder.ratingTextView.setText(String.format("⭐ %.1f", movie.getRated()));

        // Load image
        new Thread(() -> {
            try {
                URL url = new URL(movie.getPosterUrl());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                input.close();

                // Update the ImageView
                holder.itemView.post(() -> holder.posterImageView2.setImageBitmap(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        holder.itemView.setBackgroundColor(Color.WHITE);
    }

    @Override
    public int getItemCount() {
        return favouriteMovies.size();
    }

    public void updateFavourites(List<Movie> newFavourites) {
        this.favouriteMovies = newFavourites;
        notifyDataSetChanged();
    }

    // Helper method to get movie at position
    public Movie getMovieAt(int position) {
        if (position >= 0 && position < favouriteMovies.size()) {
            return favouriteMovies.get(position);
        }
        return null;
    }
}
package com.example.movie_central.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_central.R;
import com.example.movie_central.model.Movie;
import com.example.movie_central.view.ItemClickListener;
import com.example.movie_central.view.ViewHolder;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<ViewHolder> {

    // Adding the dataset
    List<Movie> items;

    Context context;

    public ItemClickListener clickListener;

    public MovieAdapter(Context context, List<Movie> items) {
        this.context = context;
        this.items = items;
    }

    public void setClickListener(ItemClickListener myListener) {
        this.clickListener = myListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(itemView, this.clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = items.get(position);
        holder.titleTextView.setText(movie.getTitle());
        holder.yearTextView.setText(movie.getYear());

        holder.typeTextView.setText(movie.getType());

        // Asynchronously load the image
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
                holder.itemView.post(() -> holder.posterImageView.setImageBitmap(bitmap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        holder.itemView.setBackgroundColor(Color.WHITE);
    }

    public void updateMovies(List<Movie> newMovies) {
        Log.d("MovieAdapter", "Reset Live Data");
        items.clear();
        items.addAll(newMovies);
        notifyDataSetChanged();
    }


    public List<Movie> getMovies() {
        return items;
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void clearMovies() {
        items.clear();
        notifyDataSetChanged();
    }
}

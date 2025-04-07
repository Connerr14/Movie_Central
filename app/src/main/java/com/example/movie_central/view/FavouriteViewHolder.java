package com.example.movie_central.view;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.movie_central.R;

public class FavouriteViewHolder extends RecyclerView.ViewHolder {

    // UI Components
    TextView titleTextView;
    TextView studioTextView;
    TextView ratingTextView;
    ImageView posterImageView2;
    Button editButton;
    Button deleteButton;

    FavouriteClickListener clickListener;

    public FavouriteViewHolder(@NonNull View itemView, FavouriteClickListener clickListener) {
        super(itemView);

        // Initialize views
        titleTextView = itemView.findViewById(R.id.movieTitle);
        studioTextView = itemView.findViewById(R.id.movieStudio);
        ratingTextView = itemView.findViewById(R.id.movieRating);
        posterImageView2 = itemView.findViewById(R.id.moviePosterImageView);
        editButton = itemView.findViewById(R.id.editButton);
        deleteButton = itemView.findViewById(R.id.deleteButton);

        if (clickListener == null) {
            throw new IllegalArgumentException("FavouriteClickListener cannot be null");
        }
        this.clickListener = clickListener;

        // Set click listeners
        itemView.setOnClickListener(view -> {
            Log.d("FavouriteViewHolder", "Item clicked at position " + getAdapterPosition());
            clickListener.onClick(view, getAdapterPosition());
        });

        editButton.setOnClickListener(view -> {
            Log.d("FavouriteViewHolder", "Edit clicked for position " + getAdapterPosition());
            clickListener.onEditClick(getAdapterPosition());
        });

        deleteButton.setOnClickListener(view -> {
            Log.d("FavouriteViewHolder", "Delete clicked for position " + getAdapterPosition());
            clickListener.onDeleteClick(getAdapterPosition());
        });
    }
}
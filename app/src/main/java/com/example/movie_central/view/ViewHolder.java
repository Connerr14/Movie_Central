package com.example.movie_central.view;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movie_central.R;

public class ViewHolder extends RecyclerView.ViewHolder {

    // Declare the components of the item
    TextView titleTextView;
    TextView yearTextView;
    TextView typeTextView;
    ImageView posterImageView;

    ItemClickListener clickListener;


    public ViewHolder(@NonNull View itemView, ItemClickListener clickListener) {

        super(itemView);
        titleTextView = itemView.findViewById(R.id.movieTitle);
        yearTextView = itemView.findViewById(R.id.yearOfRelease);
        typeTextView = itemView.findViewById(R.id.movieType);
        posterImageView = itemView.findViewById(R.id.moviePosterImageView);


        if (clickListener == null) {
            throw new IllegalArgumentException("ItemClickListener cannot be null");
        }
        this.clickListener = clickListener;


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag", "OnViewHolder Click");
                // Main activity being called here
                clickListener.onClick(view, getAdapterPosition());
            }
        });

    }
}

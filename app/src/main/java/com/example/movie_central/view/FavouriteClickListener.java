package com.example.movie_central.view;

import android.view.View;

public interface FavouriteClickListener {
    void onClick(View view, int position);      // For item clicks
    void onEditClick(int position);            // For edit actions
    void onDeleteClick(int position);          // For delete actions
}
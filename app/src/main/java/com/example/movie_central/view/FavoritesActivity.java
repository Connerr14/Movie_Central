package com.example.movie_central.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.movie_central.R;
import com.example.movie_central.databinding.ActivityFavoritesBinding;
import com.example.movie_central.databinding.ActivityMainBinding;
import com.example.movie_central.model.Movie;
import com.example.movie_central.viewmodel.MovieViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity implements ItemClickListener {
    MovieViewModel viewModel;

    ActivityFavoritesBinding binding;

    MovieAdapter movieAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorites);

        // Set up the binding
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Set up RecyclerView
        // Getting new instance of adapter
        movieAdapter = new MovieAdapter(getApplicationContext(), new ArrayList<>());
        movieAdapter.setClickListener(this);  // Set the clickListener here
        binding.favRecyclerView1.setLayoutManager(new LinearLayoutManager(this));
        binding.favRecyclerView1.setAdapter(movieAdapter);

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        Log.d("Main", "In before observable");

        // Set up a observable to watch for when a new movie is added
        viewModel.getFavoriteMoviesLiveData().observe(this, favs -> {
            movieAdapter.clearMovies();
            movieAdapter.updateMovies(favs);
        });

        // Get the current user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get the users fav movies based on there id
        viewModel.loadFavoriteMovies(user.getUid());

        binding.searchBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FavoritesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Log.d("OnResume", "In OnResume");
        // Set up a observable to watch for when a new movie is added
        viewModel.getFavoriteMoviesLiveData().observe(this, favs -> {
            movieAdapter.clearMovies();
            movieAdapter.updateMovies(favs);
        });

        // Get the users fav movies based on there id
        viewModel.loadFavoriteMovies(user.getUid());
    }



    @Override
    public void onClick(View V, int position) {
        Movie clickedMovie = movieAdapter.getMovies().get(position);

        Intent intent = new Intent(FavoritesActivity.this, FavoriteMovieDetailsActivity.class);
        intent.putExtra("KEY_ONE", clickedMovie);
        startActivity(intent);
    }

}
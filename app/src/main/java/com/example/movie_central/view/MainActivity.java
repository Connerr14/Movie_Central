package com.example.movie_central.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.UnstableApi;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.movie_central.R;
import com.example.movie_central.databinding.ActivityMainBinding;
import com.example.movie_central.model.Movie;
import com.example.movie_central.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemClickListener {
    Movie lastMovie;

    MovieViewModel viewModel;
    ActivityMainBinding binding;
    MovieAdapter movieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);



        // Set up view binding with MovieViewModel
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Set up RecyclerView
        // Getting new instance of adapter
        movieAdapter = new MovieAdapter(getApplicationContext(), new ArrayList<>());
        movieAdapter.setClickListener(this);  // Set the clickListener here
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(movieAdapter);

        // Observe LiveData from ViewModel
        viewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                lastMovie = movies.get(movies.size() - 1);
                movieAdapter.updateMovies(movies);
            }
        });


        // When the button is clicked, search for the text in the search button
        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Trigger search in ViewModel
                lastMovie = null;
                movieAdapter.clearMovies(); // Clear old results
                movieAdapter.notifyDataSetChanged(); // Ensure UI updates
                String query = binding.txtSearch.getText().toString();
                viewModel.Search(query);  // Pass the query to search
            }
        });

        // Observe the movie details LiveData
        viewModel.getMovieDetails().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie)
            {
                lastMovie = movie;
                Intent intent = new Intent(MainActivity.this, OutputActivity.class);
                intent.putExtra("KEY_ONE", movie); // Pass movie data
                startActivity(intent);

            }
        });

        binding.favoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to the favorites page
                Intent intent = new Intent(MainActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });

    }

    @OptIn(markerClass = UnstableApi.class)
    @Override
    public void onClick(View v, int pos) {
        Movie clickedMovie = movieAdapter.getMovies().get(pos);

         viewModel.fetchMovieDetails(clickedMovie.getImdbID());

        Toast.makeText(this, "You Choose: " + clickedMovie.getTitle(), Toast.LENGTH_SHORT).show();

    }
}
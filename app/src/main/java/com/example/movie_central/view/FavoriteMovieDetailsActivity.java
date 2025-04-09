package com.example.movie_central.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.movie_central.R;
import com.example.movie_central.databinding.ActivityFavoriteMovieDetailsBinding;
import com.example.movie_central.model.Movie;
import com.example.movie_central.viewmodel.MovieViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FavoriteMovieDetailsActivity extends AppCompatActivity {
    ActivityFavoriteMovieDetailsBinding binding;

    MovieViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite_movie_details);

        // Set up the binding
        binding = ActivityFavoriteMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);


        Movie movie = getIntent().getParcelableExtra("KEY_ONE", Movie.class);


        // Set the movie details into the views using View Binding
        binding.movieTitle1.setText(movie.getTitle());
        binding.movieYear1.setText(String.format("Year: %s", movie.getYear()));
        binding.movieGenre1.setText(String.format("Genre: %s", movie.getGenre()));
        binding.movieDirector1.setText(String.format("Director: %s", movie.getDirector()));
        binding.moviePlot1.setText(String.format("Plot: %s", movie.getPlot()));
        binding.rated.setText(String.format("Rated: %s", movie.getRated()));
        loadMoviePoster(movie.getPosterUrl());

        viewModel.getMovieDescription(movie.getImdbID(), description -> {
            binding.movieDescription.setText(description);
        });



        // Set up the back button to close the activity
        binding.btnBack1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();  // Close the activity and go back to the previous screen
            }
        });

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = String.valueOf(binding.movieDescription.getText());
                viewModel.updateMovieDescription(description, movie.getImdbID());

            }
        });

        binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("favoriteMovieDetailsActivity","In Delete" );
                viewModel.removeFavoriteMovie(movie.getImdbID());
                Intent intent = new Intent(FavoriteMovieDetailsActivity.this, MainActivity.class );
                startActivity(intent);
            }
        });
    }


    private void loadMoviePoster(String posterUrl) {
        new Thread(() -> {
            try {
                URL url = new URL(posterUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                input.close();

                // Update the ImageView
                runOnUiThread(() -> binding.moviePoster1.setImageBitmap(bitmap));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
}
package com.example.movie_central.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.movie_central.R;
import com.example.movie_central.databinding.ActivityMovieDetailsBinding;
import com.example.movie_central.model.Movie;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class OutputActivity extends AppCompatActivity {
    TextView movieTitle;
    Button backButton;

    private ActivityMovieDetailsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize View Binding
        binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the Movie object passed from MainActivity
        Movie movie = getIntent().getParcelableExtra("KEY_ONE", Movie.class);

        if (movie != null) {
            // Set the movie details into the views using View Binding
            binding.movieTitle1.setText(movie.getTitle());
            binding.movieYear1.setText(String.format("Year: %s", movie.getYear()));
            binding.movieGenre1.setText(String.format("Genre: %s", movie.getGenre()));
            binding.movieDirector1.setText(String.format("Director: %s", movie.getDirector()));
            binding.moviePlot1.setText(String.format("Plot: %s", movie.getPlot()));
            binding.movieImdbRating1.setText(String.format("IMDb Rating: %s", movie.getImdbRating()));
            binding.rated.setText(String.format("Rated: %s", movie.getRated()));
            loadMoviePoster(movie.getPosterUrl());


            // Set up the back button to close the activity
            binding.btnBack1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();  // Close the activity and go back to the previous screen
                }
            });
        }
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




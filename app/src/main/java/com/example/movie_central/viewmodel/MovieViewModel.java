package com.example.movie_central.viewmodel;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.movie_central.model.Movie;
import com.example.movie_central.model.User;
import com.example.movie_central.utils.APIClient;
import com.example.movie_central.view.LoginActivity;
import com.example.movie_central.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieViewModel extends ViewModel {
    private FirebaseAuth mAuth;

    List<Movie> movies = new ArrayList<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    private final MutableLiveData<List<Movie>> moviesLiveData = new MutableLiveData<>();
    private final MutableLiveData<Movie> movieDetailsLiveData = new MutableLiveData<>(); // For individual movie details
    public LiveData<List<Movie>> getMovies() {
        return moviesLiveData;
    }

    public LiveData<Movie> getMovieDetails() {
        return movieDetailsLiveData; // Return LiveData for the movie details
    }

    public void addMovie(Movie movie) {
        List<Movie> currentMovies = moviesLiveData.getValue();
        if (currentMovies == null) {
            currentMovies = new ArrayList<>();
        }
        currentMovies.add(movie);  // Add the new movie to the list
    }

    public void Search(String query) {
        movies.clear();
        String API_KEY = "9070f20c";
        String BASE_URL = "https://www.omdbapi.com/?apikey=" + API_KEY + "&s=";

        BASE_URL = BASE_URL + query;

        Log.d("MainActivity", "Endpoint" + BASE_URL);


        // Call the API
        APIClient.get(BASE_URL, new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // Parse the results, add them to the Live Data of movie lists
                if (response.body() == null) {
                    Log.d("MovieViewModel", "No Results");
                    return;
                }

                String responseData = response.body().string();

                try {
                    JSONObject json = new JSONObject(responseData);
                    JSONArray searchArray = json.optJSONArray("Search");

                    if (searchArray != null) {

                        for (int i = 0; i < searchArray.length(); i++) {
                            JSONObject movieObj = searchArray.getJSONObject(i);

                            String title = movieObj.optString("Title");
                            String year = movieObj.optString("Year");

                            String imdbID = movieObj.optString("imdbID");

                            String posterUrl = movieObj.optString("Poster");
                            String type = movieObj.optString("Type");

                            Log.d("MainActivity", "Created objects" + type);

                            movies.add(new Movie(title, year, imdbID, type, null, null, null,  null, posterUrl, null, null));
                        }

                        moviesLiveData.postValue(movies);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

        });
    }
    public void fetchMovieDetails(String imdbID) {
        String API_KEY = "9070f20c";
        String BASE_URL = "https://www.omdbapi.com/?apikey=" + API_KEY + "&i=" + imdbID;

        APIClient.get(BASE_URL, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() == null) {
                    Log.d("MovieViewModel", "No Details Found for ID: " + imdbID);
                    return;
                }

                String responseData = response.body().string();
                try {
                    JSONObject movieObj = new JSONObject(responseData);

                    String title = movieObj.optString("Title", "N/A");
                    String year = movieObj.optString("Year", "N/A");
                    String imdbID = movieObj.optString("imdbID", "N/A");
                    String type = movieObj.optString("Type", "N/A");
                    String imdbRating = movieObj.optString("imdbRating", "N/A");
                    String genre = movieObj.optString("Genre", "N/A");
                    String director = movieObj.optString("Director", "N/A");
                    String plot = movieObj.optString("Plot", "N/A");
                    String posterUrl = movieObj.optString("Poster", "N/A");
                    String writer = movieObj.optString("Writer", "N/A");
                    String rated = movieObj.optString("Rated", "N/A");

                    // Create a Movie object with the retrieved values
                    Movie movie = new Movie(title, year, imdbID, type, imdbRating, genre, director, plot, posterUrl, writer, rated);
                    movieDetailsLiveData.postValue(movie);
                } catch (JSONException e) {
                    Log.e("MovieViewModel", "JSON Parsing Error", e);
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("MovieViewModel", "API Call Failure", e);
            }
        });
    }

    // A method to add a user to the DB (favMovies needs to be initialized with null)
    public void addUser(String email, String password, String[] favMovies, AuthCallBack callback) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        User user = new User(email, null, favMovies);

                        db.collection("users").document(userId).set(user)
                                .addOnSuccessListener(aVoid -> callback.onResult(true, null))
                                .addOnFailureListener(e -> callback.onResult(false, e.getMessage()));
                    } else {
                        callback.onResult(false, task.getException().getMessage());
                    }
                });
    }

    // A method to get the users details from the db
    public void getUserDetails () {
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String username = document.getString("username");
                    String password = document.getString("password");
                    Log.d("MainActivity", "User: " + username + ", Password: " + password);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("MainActivity", "Error getting users", e);
            }
        });
    }

    // A method to add a favorite movie to a users account (Fix to not allow duplicates)
    public void addFavoriteMovie(String userId, String imdbID) {
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {

                userRef.update("favMovies", FieldValue.arrayUnion(imdbID))
                        .addOnSuccessListener(aVoid -> Log.d("AddFav", "Movie added to favorites"))
                        .addOnFailureListener(e -> Log.e("AddFav", "Failed to add movie", e));
            } else {
                Map<String, Object> newUser = new HashMap<>();
                newUser.put("favMovies", Arrays.asList(imdbID));
                newUser.put("username", FirebaseAuth.getInstance().getCurrentUser().getEmail());

                userRef.set(newUser)
                        .addOnSuccessListener(aVoid -> Log.d("AddFav", "User doc created + movie added"))
                        .addOnFailureListener(e -> Log.e("AddFav", "Failed to create user doc", e));
            }
        });
    }

    public void removeFavoriteMovie(String userId, String imdbID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.update("favMovies", FieldValue.arrayRemove(imdbID))
                .addOnSuccessListener(aVoid -> Log.d("MainActivity", "Movie removed from favorites"))
                .addOnFailureListener(e -> Log.e("MainActivity", "Failed to remove movie", e));
    }

    public void logUserIn(String email, String password, AuthCallBack callback) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onResult(true, null);
                    } else {
                        callback.onResult(false, task.getException().getMessage());
                    }
                });
    }


}




package com.example.movie_central.model;

public class User {

    public String username;

    public String password;

    // Storing by ImDb
    public String[] favMovies;

    public User(String username, String password, String[] favMovies) {
        this.username = username;
        this.password = password;
        this.favMovies = favMovies;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getFavMovies() {
        return favMovies;
    }

    public void setFavMovies(String[] favMovies) {
        this.favMovies = favMovies;
    }

}

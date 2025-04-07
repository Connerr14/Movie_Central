package com.example.movie_central.model;

import java.io.Serializable;

// Implements Serializable so we can pass the Movie object via Intent
public class Movie implements Serializable {
    private final String title;
    private final String year;
    private final String imdbID;

    private final String type;
    private final String imdbRating;
    private final String genre;
    private final String director;
    private final String plot;
    private final String posterUrl;



    private final String writer;

    private final String rated;

    // Constructor
    public Movie(String title, String year, String imdbID, String type, String imdbRating,
                 String genre, String director, String plot, String posterUrl, String writer, String rated) {
        this.title = title != null ? title : "N/A";
        this.year = year != null ? year : "N/A";
        this.imdbID = imdbID != null ? imdbID : "N/A";
        this.imdbRating = imdbRating != null ? imdbRating : "N/A";
        this.genre = genre != null ? genre : "N/A";
        this.director = director != null ? director : "N/A";
        this.writer = writer != null ? writer : "N/A";
        this.type = type != null ? type : "N/A";;
        this.plot = plot != null ? plot : "N/A";
        this.posterUrl = posterUrl != null ? posterUrl : "N/A";
        this.rated = rated != null ? rated : "N/A";;
    }

    // Getters for the fields
    public String getTitle() {
        return title;
    }

    public String getYear() {
        return year;
    }

    public String getImdbID() {
        return imdbID;
    }

    public String getImdbRating() {
        return imdbRating;
    }

    public String getGenre() {
        return genre;
    }

    public String getDirector() {
        return director;
    }

    public String getPlot() {
        return plot;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getType() {
        return type;
    }

    public String getWriter() {
        return writer;
    }

    public String getRated() {
        return rated;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", imdbID='" + imdbID + '\'' +
                ", type='" + type + '\'' +
                ", imdbRating='" + imdbRating + '\'' +
                ", genre='" + genre + '\'' +
                ", director='" + director + '\'' +
                ", plot='" + plot + '\'' +
                ", posterUrl='" + posterUrl + '\'' +
                ", writer='" + writer + '\'' +
                ", rated='" + rated + '\'' +
                '}';
    }
}
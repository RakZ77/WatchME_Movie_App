package kh.edu.rupp.watchme.models;

import java.util.List;

public class MovieSection {
    private String title;
    private List<Movie> movies;

    public MovieSection(String title, List<Movie> movies) {
        this.title = title;
        this.movies = movies;
    }

    public String getTitle() { return title; }
    public List<Movie> getMovies() { return movies; }
}


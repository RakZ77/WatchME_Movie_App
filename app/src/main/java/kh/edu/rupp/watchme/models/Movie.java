package kh.edu.rupp.watchme.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("title")
    private String title;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("overview")
    private String overview;
    @SerializedName("backdrop_path")
    private String backdrop_path;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("vote_average")
    private double vote_average;
    @SerializedName("genres")
    private List<Genre> genres;

    private int runtime;

    public String getTitle() { return title; }
    public String getPosterPath() { return poster_path; }

    public int getId(){return id; }
    public String getOverview() { return overview; }
    public String getBackdropPath() { return backdrop_path; }
    public String getReleaseDate() { return release_date; }
    public double getVoteAverage() { return vote_average; }
    public List<Genre> getGenre() { return genres; }
    public int getRuntime() { return runtime; }

    public String getGenreNames() {
        if (genres == null || genres.isEmpty()) return "";
        StringBuilder names = new StringBuilder();
        for (int i = 0; i < genres.size(); i++) {
            names.append(genres.get(i).getName());
            if (i < genres.size() - 1) names.append(", ");
        }
        return names.toString();
    }

}

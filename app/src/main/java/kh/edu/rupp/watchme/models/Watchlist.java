package kh.edu.rupp.watchme.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "watch_list")
public class Watchlist {
    @PrimaryKey
    public int id;
    public String title;
    public String posterPath;
    public double rating;
    public String genre;
    public String releaseDate;
    public int runtime;

    public Watchlist(int id, String title, String posterPath, double rating, String genre, String releaseDate, int runtime) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.rating = rating;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
    }

    public String getTitle() { return title; }
    public String getPosterPath() { return posterPath; }
    public double getRating() { return rating; }
    public String getGenre() { return genre; }
    public String getReleaseDate() { return releaseDate; }
    public int getRuntime() { return runtime; }
    public int getId() { return id; }
}

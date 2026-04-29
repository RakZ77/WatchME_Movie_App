package kh.edu.rupp.watchme.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "watch_list",
        primaryKeys = {"id", "userId"})
public class Watchlist {
    public int id;
    @NonNull
    public String userId;
    public String title;
    public String posterPath;
    public double rating;
    public String genre;
    public String releaseDate;
    public int runtime;

    public Watchlist(int id, String userId, String title, String posterPath, double rating, String genre, String releaseDate, int runtime) {
        this.id = id;
        this.userId = userId;
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

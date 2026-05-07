package kh.edu.rupp.watchme.models;

import com.google.gson.annotations.SerializedName;

public class ReviewRequest {
    @SerializedName("user_id")
    private String userId;

    @SerializedName("movie_id")
    private int movieId;

    @SerializedName("review")
    private String comment;

    public ReviewRequest(String userId, int movieId, String comment) {
        this.userId = userId;
        this.movieId = movieId;
        this.comment = comment;
    }
}

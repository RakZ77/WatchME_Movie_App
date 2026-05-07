package kh.edu.rupp.watchme.models;

import com.google.gson.annotations.SerializedName;

public class Review {
    @SerializedName("id")
    private String id;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("movie_id")
    private int movieId;

    @SerializedName("review")
    private String comment;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("profiles")
    private Profiles profiles;

    public Review(String userId, int movieId, String comment) {
        this.userId = userId;
        this.movieId = movieId;
        this.comment = comment;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public int getMovieId() { return movieId; }
    public String getComment() { return comment; }
    public String getCreatedAt() { return createdAt; }
    public Profiles getProfiles()  { return profiles; }

    public String getDisplayName() {
        return profiles.getUsername();
    }
    public String getAvatarUrl() {
        return profiles != null ? profiles.getAvatar_url() : null;
    }

    public void setProfiles(Profiles profiles) {
        this.profiles = profiles;
    }

}
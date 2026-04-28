package kh.edu.rupp.watchme.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @SerializedName("genre_ids")
    private List<Integer> genreIds;
    @SerializedName("production_companies")
    private List<ProductionCompanies> productionCompanies;
    @SerializedName("runtime")
    private int runtime;

    public String getTitle() { return title; }
    public String getPosterPath() { return poster_path; }

    public int getId(){return id; }
    public String getOverview() { return overview; }
    public String getBackdropPath() { return backdrop_path; }
    public String getReleaseDate() { return release_date; }
    public double getVoteAverage() { return vote_average; }
    public int getRuntime() { return runtime; }

    public String getProductionCompanies() {
        if (productionCompanies == null || productionCompanies.isEmpty()) return "Unknown"; // ← was checking genres!
        StringBuilder companyNames = new StringBuilder();
        for (ProductionCompanies company : productionCompanies) {
            companyNames.append(company.getName()).append(", ");
        }
        return companyNames.toString();
    }

    private static final Map<Integer, String> GENRE_MAP = new HashMap<Integer, String>() {{
        put(28, "Action");
        put(12, "Adventure");
        put(16, "Animation");
        put(35, "Comedy");
        put(80, "Crime");
        put(99, "Documentary");
        put(18, "Drama");
        put(10751, "Family");
        put(14, "Fantasy");
        put(36, "History");
        put(27, "Horror");
        put(10402, "Music");
        put(9648, "Mystery");
        put(10749, "Romance");
        put(878, "Sci-Fi");
        put(10770, "TV Movie");
        put(53, "Thriller");
        put(10752, "War");
        put(37, "Western");
    }};
    public String getGenreNames() {
        if (genres != null && !genres.isEmpty()){
            StringBuilder names = new StringBuilder();
            int limit = Math.min(genres.size(), 3);

            for (int i = 0; i < limit; i++) {
                names.append(genres.get(i).getName());
                if (i < limit - 1) names.append(", ");
            }

            return names.toString();
        }

        if (genreIds != null && !genreIds.isEmpty()) {
            StringBuilder names = new StringBuilder();
            int limit = Math.min(genreIds.size(), 3);
            for (int i = 0; i < limit; i++) {
                String name = GENRE_MAP.get(genreIds.get(i));
                if (name != null) {
                    names.append(name);
                    if (i < limit - 1) names.append(", ");
                }
            }
            return names.toString();
        }
        return "Unknown";
    }

}

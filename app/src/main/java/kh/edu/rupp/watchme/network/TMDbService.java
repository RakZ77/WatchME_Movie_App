package kh.edu.rupp.watchme.network;

import kh.edu.rupp.watchme.models.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDbService {
    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey
    );
}

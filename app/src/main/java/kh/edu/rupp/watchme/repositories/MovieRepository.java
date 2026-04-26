package kh.edu.rupp.watchme.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import kh.edu.rupp.watchme.BuildConfig;
import kh.edu.rupp.watchme.models.Movie;
import kh.edu.rupp.watchme.models.MovieResponse;
import kh.edu.rupp.watchme.network.RetrofitClient;
import kh.edu.rupp.watchme.network.TMDbService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private TMDbService api;
    private MutableLiveData<List<Movie>> movies = new MutableLiveData<>();

    public MovieRepository(){
        api = RetrofitClient.getTMDbService();
    }

    public LiveData<List<Movie>> getPopularMovies(){
        api.getPopularMovies(BuildConfig.TMDB_API_KEY)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if(response.isSuccessful() && response.body() != null){
                            movies.setValue(response.body().getResults());
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        movies.setValue(null);
                    }
                });
        return movies;
    }

}

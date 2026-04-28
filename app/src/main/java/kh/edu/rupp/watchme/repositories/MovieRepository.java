package kh.edu.rupp.watchme.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import kh.edu.rupp.watchme.BuildConfig;
import kh.edu.rupp.watchme.models.CreditsResponse;
import kh.edu.rupp.watchme.models.CrewMember;
import kh.edu.rupp.watchme.models.Movie;
import kh.edu.rupp.watchme.models.MovieResponse;
import kh.edu.rupp.watchme.models.Video;
import kh.edu.rupp.watchme.models.VideoResponse;
import kh.edu.rupp.watchme.network.RetrofitClient;
import kh.edu.rupp.watchme.network.TMDbService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private TMDbService api;

    public MovieRepository(){
        api = RetrofitClient.getTMDbService();
    }

    public LiveData<List<Movie>> getMovies(String category) {
        MutableLiveData<List<Movie>> data = new MutableLiveData<>();
        Call<MovieResponse> call;

        switch (category) {
            case "now_playing":
                call = api.getNowPlayingMovies(BuildConfig.TMDB_API_KEY);
                break;
            case "top_rated":
                call = api.getTopRatedMovies(BuildConfig.TMDB_API_KEY);
                break;
            case "upcoming":
                call = api.getUpcomingMovies(BuildConfig.TMDB_API_KEY);
                break;
            default:
                call = api.getPopularMovies(BuildConfig.TMDB_API_KEY);
                break;
        }

        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    data.setValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

    public LiveData<Movie> getMovieDetails(int movieId) {
        MutableLiveData<Movie> movieDetail = new MutableLiveData<>();
        api.getMovieDetails(movieId, BuildConfig.TMDB_API_KEY)
                .enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Call<Movie> call, Response<Movie> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            movieDetail.setValue(response.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<Movie> call, Throwable t) {
                        movieDetail.setValue(null);
                    }
                });
        return movieDetail;
    }

    public LiveData<List<Video>> getMovieVideos(int movieId) {
        MutableLiveData<List<Video>> videos = new MutableLiveData<>();
        api.getMovieVideos(movieId, BuildConfig.TMDB_API_KEY)
                .enqueue(new Callback<VideoResponse>() {
                    @Override
                    public void onResponse(Call<VideoResponse> call, Response<VideoResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            videos.setValue(response.body().getResults());
                        } else {
                            videos.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<VideoResponse> call, Throwable t) {
                        videos.setValue(null);
                    }
                });
        return videos;
    }

    public LiveData<String> getMovieDirector(int movieId) {
        MutableLiveData<String> director = new MutableLiveData<>();

        api.getMovieCredits(movieId, BuildConfig.TMDB_API_KEY)
                .enqueue(new Callback<CreditsResponse>() {
                    @Override
                    public void onResponse(Call<CreditsResponse> call, Response<CreditsResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Find the person with job = "Director" in crew list
                            for (CrewMember member : response.body().getCrew()) {
                                if ("Director".equals(member.getJob())) {
                                    director.setValue(member.getName());
                                    return;
                                }
                            }
                            director.setValue("Unknown");
                        }
                    }

                    @Override
                    public void onFailure(Call<CreditsResponse> call, Throwable t) {
                        director.setValue("Unknown");
                    }
                });

        return director;
    }
}

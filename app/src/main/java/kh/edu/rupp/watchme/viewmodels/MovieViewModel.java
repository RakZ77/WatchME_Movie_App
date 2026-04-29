package kh.edu.rupp.watchme.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import kh.edu.rupp.watchme.database.WatchlistDatabase;
import kh.edu.rupp.watchme.models.Movie;
import kh.edu.rupp.watchme.models.Video;
import kh.edu.rupp.watchme.models.Watchlist;
import kh.edu.rupp.watchme.repositories.MovieRepository;

public class MovieViewModel extends AndroidViewModel {
    private MovieRepository repo;
    private MutableLiveData<List<Movie>> movies;

    public MovieViewModel(Application application) {
        super(application);
        repo = new MovieRepository(application);
    }

    public LiveData<List<Movie>> getMovies(String category, int page) {
        return repo.getMovies(category, page);
    }

    public LiveData<Movie> getMovieDetails(int movieId) {
        return repo.getMovieDetails(movieId);
    }

    public LiveData<List<Video>> getMovieVideos(int movieId) {
        return repo.getMovieVideos(movieId);
    }

    public LiveData<String> getMovieDirector(int movieId) {
        return repo.getMovieDirector(movieId);
    }

    public void addToWatchlist(Watchlist movie) {
        repo.addToWatchlist(movie);
    }

    public void removeFromWatchlist(Watchlist movie) {
        repo.removeFromWatchlist(movie);
    }

    public LiveData<Boolean> isInWatchlist(int id, String userId) {
        return repo.isInWatchlist(id, userId);
    }
    public LiveData<List<Watchlist>> getWatchlist(String userId) {
        return repo.getWatchlist(userId);
    }
    public LiveData<List<Movie>> searchMovies(String query, int page) {
        return repo.searchMovies(query, page);
    }
}

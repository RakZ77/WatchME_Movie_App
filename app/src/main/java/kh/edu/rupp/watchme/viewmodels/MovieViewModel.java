package kh.edu.rupp.watchme.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import kh.edu.rupp.watchme.models.Movie;
import kh.edu.rupp.watchme.repositories.MovieRepository;

public class MovieViewModel extends AndroidViewModel {
    private MovieRepository repo;
    private MutableLiveData<List<Movie>> movies;

    public MovieViewModel(Application application) {
        super(application);
        repo = new MovieRepository();
    }

    public LiveData<List<Movie>> getMovies(){
        if (movies == null) {
            movies = (MutableLiveData<List<Movie>>) repo.getPopularMovies();
        }
        return movies;
    }

}

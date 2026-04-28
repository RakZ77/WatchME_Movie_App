package kh.edu.rupp.watchme.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.models.Movie;
import kh.edu.rupp.watchme.viewmodels.MovieViewModel;

public class CategoryMovieAdapter extends RecyclerView.Adapter<CategoryMovieAdapter.MovieViewHolder> {
    private List<Movie> movies = new ArrayList<>();
    private OnMovieClickListener listener;
    private MovieViewModel viewModel;
    private LifecycleOwner lifecycleOwner;


    public CategoryMovieAdapter(List<Movie> movies, OnMovieClickListener listener, MovieViewModel viewModel, LifecycleOwner lifecycleOwner) {
        this.movies = movies;
        this.listener = listener;
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_categorical_movie_view, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie =movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMovieTitle, tvDirector, tvGenre;
        private ImageView moviePoster;
        private int currentMovieId = -1;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvDirector = itemView.findViewById(R.id.tvDirector);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            tvGenre = itemView.findViewById(R.id.tvGenre);
        }

        public void bind(Movie movie){
            currentMovieId = movie.getId();
            this.tvMovieTitle.setText(movie.getTitle());
            this.tvGenre.setText(movie.getGenreNames());
            this.tvDirector.setText("Loading...");

            viewModel.getMovieDirector(movie.getId()).observe(lifecycleOwner, director -> {
                if (currentMovieId == movie.getId() && director != null) {
                    tvDirector.setText(director);
                }
            });


            String imageUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
            Picasso.get().load(imageUrl).into(moviePoster);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMovieClick(movie);
                }
            });
        }
    }
}

package kh.edu.rupp.watchme.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.models.Movie;
import kh.edu.rupp.watchme.viewmodels.MovieViewModel;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private List<Movie> movies;
    private OnMovieClickListener listener;
    private MovieViewModel viewModel;
    private LifecycleOwner lifecycleOwner;

    public SearchAdapter(List<Movie> movies, OnMovieClickListener listener, MovieViewModel viewModel, LifecycleOwner lifecycleOwner) {
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
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_view, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Movie movie =movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {
        private ImageView moviePoster;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
        }

        public void bind(Movie movie){
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

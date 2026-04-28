package kh.edu.rupp.watchme.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.models.Movie;
import kh.edu.rupp.watchme.models.Watchlist;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.ViewHolder> {

    private List<Watchlist> list;
    private OnMovieClickListener listener;

    public WatchlistAdapter(List<Watchlist> list, OnMovieClickListener listener){
        this.list = list;
        this.listener = listener;
    }

    public interface OnMovieClickListener {
        void onMovieClick(Watchlist movie);
    }

    public void setData(List<Watchlist> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watch_list_movie_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Watchlist movie = list.get(position);

        holder.title.setText(movie.getTitle());
        holder.rating.setText(String.format("%.1f", movie.getRating()));
        holder.runtime.setText(movie.getRuntime() + " mins");
        holder.genre.setText(movie.getGenre());
        holder.publishDate.setText(movie.getReleaseDate());


        // Load image (Glide or Picasso)
        String url = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();

        Picasso.get().load(url).into(holder.poster);


        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMovieClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title, rating, runtime, genre, publishDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            poster = itemView.findViewById(R.id.moviePoster);
            title = itemView.findViewById(R.id.tvMovieTitle);
            rating = itemView.findViewById(R.id.tvRating);
            runtime = itemView.findViewById(R.id.tvDuration);
            genre = itemView.findViewById(R.id.tvGenre);
            publishDate = itemView.findViewById(R.id.tvPublishDate);
        }
    }
}

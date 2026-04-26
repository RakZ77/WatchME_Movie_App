package kh.edu.rupp.watchme.views;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.models.Movie;

public class DetailsAboutMovieActivity extends AppCompatActivity {
    private ImageView moviePoster, coverImage;
    private TextView tvRating, tvMovieTitle, tvPublishDate, tvDuration, tvGenre, tvOverview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_about_movie);

        // Initialize all views
        tvRating = findViewById(R.id.tvRating);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvPublishDate = findViewById(R.id.tvPublishDate);
        tvDuration = findViewById(R.id.tvDuration);
        tvGenre = findViewById(R.id.tvGenre);
        tvOverview = findViewById(R.id.tvOverview);
        moviePoster = findViewById(R.id.moviePoster);
        coverImage = findViewById(R.id.headerImage);

        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

        if (movie != null) {
            tvMovieTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());
            tvPublishDate.setText(movie.getReleaseDate());
            tvRating.setText(String.format("%.1f", movie.getVoteAverage()));
            tvDuration.setText(movie.getRuntime() + " mins");
            tvGenre.setText(movie.getGenreNames());

            // Images
            String posterUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
            Picasso.get().load(posterUrl).into(moviePoster);

            String coverUrl = "https://image.tmdb.org/t/p/w500" + movie.getBackdropPath();
            Picasso.get().load(coverUrl).into(coverImage);
        }
    }
}

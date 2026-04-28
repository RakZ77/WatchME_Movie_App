package kh.edu.rupp.watchme.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.models.Movie;
import kh.edu.rupp.watchme.models.Video;
import kh.edu.rupp.watchme.viewmodels.MovieViewModel;

public class DetailsAboutMovieActivity extends AppCompatActivity {
    private ImageView moviePoster, coverImage, btnBack, btnSave, btnPlay;
    private TextView tvRating, tvMovieTitle, tvPublishDate, tvDuration, tvGenre, tvOverview;
    private MovieViewModel viewModel;
    private String trailerKey = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_about_movie);

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Initialize all views
        tvRating = findViewById(R.id.tvRating);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvPublishDate = findViewById(R.id.tvPublishDate);
        tvDuration = findViewById(R.id.tvDuration);
        tvGenre = findViewById(R.id.tvGenre);
        tvOverview = findViewById(R.id.tvOverview);
        moviePoster = findViewById(R.id.moviePoster);
        coverImage = findViewById(R.id.headerImage);
        btnBack = findViewById(R.id.btnBack);
        btnPlay = findViewById(R.id.playButton);

        int movieId = getIntent().getIntExtra("movie_id", -1);

        if(movieId != -1){
            viewModel.getMovieDetails(movieId).observe(this, movie -> {
                if(movie != null){
                    tvMovieTitle.setText(movie.getTitle());
                    tvOverview.setText(movie.getOverview());
                    tvPublishDate.setText(movie.getReleaseDate());
                    tvRating.setText(String.format("%.1f", movie.getVoteAverage()));
                    tvDuration.setText(movie.getRuntime() + " mins");
                    tvGenre.setText(movie.getGenreNames());

                    Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.getPosterPath()).into(moviePoster);
                    Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.getBackdropPath()).into(coverImage);
                }

                viewModel.getMovieVideos(movie.getId()).observe(this, videos -> {if (videos != null) {
                    for (Video video : videos) {
                        if ("Trailer".equals(video.getType())) {
                            trailerKey = video.getKey();
                            break;
                        }
                    }
                }
                });
            });
        }

        btnBack.setOnClickListener(v -> finish());

        btnPlay.setOnClickListener(v -> {
            if (trailerKey != null) {
                Intent intent = new Intent(this, VideoPlayerActivity.class);
                intent.putExtra("trailer_key", trailerKey);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Trailer not available", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package kh.edu.rupp.watchme.views;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.adapters.ViewPagerAdapter;
import kh.edu.rupp.watchme.models.AuthResponse;
import kh.edu.rupp.watchme.models.Movie;
import kh.edu.rupp.watchme.models.Video;
import kh.edu.rupp.watchme.models.Watchlist;
import kh.edu.rupp.watchme.network.RetrofitClient;
import kh.edu.rupp.watchme.network.SupabaseService;
import kh.edu.rupp.watchme.utils.SessionManager;
import kh.edu.rupp.watchme.viewmodels.MovieViewModel;

public class DetailsAboutMovieActivity extends AppCompatActivity {
    private ImageView moviePoster, coverImage, btnBack, btnPlay;
    private TextView tvRating, tvMovieTitle, tvPublishDate, tvDuration, tvGenre;
    private MovieViewModel viewModel;
    private String trailerKey = null;
    private ImageButton btnSave;
    private Movie currentMovie;
    private boolean isInWatchlist = false;
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

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
        moviePoster = findViewById(R.id.moviePoster);
        coverImage = findViewById(R.id.headerImage);
        btnBack = findViewById(R.id.btnBack);
        btnPlay = findViewById(R.id.playButton);
        btnSave = findViewById(R.id.btnSave);

        tabLayout = findViewById(R.id.detailView_TabLayout);
        viewPager2 = findViewById(R.id.viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Set up ViewPager2 to show selected tab
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        int movieId = getIntent().getIntExtra("movie_id", -1);
        viewPagerAdapter = new ViewPagerAdapter(this, movieId);
        viewPager2.setAdapter(viewPagerAdapter);

        if(movieId != -1){
            viewModel.getMovieDetails(movieId).observe(this, movie -> {
                if(movie != null){
                    currentMovie = movie;
                    tvMovieTitle.setText(movie.getTitle());
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
                intent.putExtra("movie_id", movieId);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Trailer not available", Toast.LENGTH_SHORT).show();
            }
        });

        final boolean[] isSavedState = {false};

        SessionManager sessionManager = new SessionManager(this);
        String userId = sessionManager.getUserId();

        viewModel.isInWatchlist(movieId, userId).observe(this, saved  -> {
            isInWatchlist = saved != null && saved;
            btnSave.setImageResource(
                    isInWatchlist ? R.drawable.ic_save_filled : R.drawable.ic_save_outlined
            );
        });

        btnSave.setOnClickListener(v -> {
            if (currentMovie == null) return;

            Watchlist item = new Watchlist(
                    currentMovie.getId(),
                    userId,
                    currentMovie.getTitle(),
                    currentMovie.getPosterPath(),
                    currentMovie.getVoteAverage(),
                    currentMovie.getGenreNames(),
                    currentMovie.getReleaseDate(),
                    currentMovie.getRuntime()
            );

            if (isInWatchlist) {
                viewModel.removeFromWatchlist(item);
            } else {
                viewModel.addToWatchlist(item);
            }
        });
    }
}

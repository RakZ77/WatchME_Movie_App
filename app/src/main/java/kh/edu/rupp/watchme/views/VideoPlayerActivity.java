package kh.edu.rupp.watchme.views;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import kh.edu.rupp.watchme.R;

public class VideoPlayerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        YouTubePlayerView playerView = findViewById(R.id.youtubePlayer);
        getLifecycle().addObserver(playerView);
        ImageView btnBack = findViewById(R.id.btnBack);

        String trailerKey = getIntent().getStringExtra("trailer_key");

        playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                if (trailerKey != null) {
                    youTubePlayer.loadVideo(trailerKey, 0);
                }
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }
}

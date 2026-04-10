package kh.edu.rupp.watchme.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import kh.edu.rupp.watchme.R;

public class LoadingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        ImageView    loadingBar = findViewById(R.id.loadingBar);
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        loadingBar.startAnimation(rotate);

        new Handler().postDelayed(() -> {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        }, 2000); // 2 seconds
    }
}

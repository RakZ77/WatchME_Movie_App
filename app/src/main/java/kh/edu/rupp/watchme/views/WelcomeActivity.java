package kh.edu.rupp.watchme.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import kh.edu.rupp.watchme.R;

public class WelcomeActivity extends AppCompatActivity {
    MaterialButton getStartedBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        getStartedBtn = findViewById(R.id.btnGetStarted);
        getStartedBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, FirstActivity.class));
            finish();
        });

    }
}

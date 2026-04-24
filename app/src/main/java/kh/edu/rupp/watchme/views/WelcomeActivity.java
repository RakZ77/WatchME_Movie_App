package kh.edu.rupp.watchme.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.viewmodels.AuthViewModel;

public class WelcomeActivity extends AppCompatActivity {
    MaterialButton getStartedBtn;
    AuthViewModel viewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        getStartedBtn = findViewById(R.id.btnGetStarted);

        getStartedBtn.setOnClickListener(v -> {
            if(viewModel.isSignedIn()){
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }else {
                startActivity(new Intent(this, SignInActivity.class));
                finish();
            }
        });

    }
}

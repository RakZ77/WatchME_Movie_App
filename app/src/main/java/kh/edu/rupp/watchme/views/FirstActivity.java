package kh.edu.rupp.watchme.views;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import kh.edu.rupp.watchme.R;

public class FirstActivity extends AppCompatActivity {
    MaterialButton signInBtn, signUpBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        signInBtn = findViewById(R.id.btnSignIn);
        signUpBtn = findViewById(R.id.btnSignUp);

        signInBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInActivity.class));
        });

        signUpBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });

    }
}

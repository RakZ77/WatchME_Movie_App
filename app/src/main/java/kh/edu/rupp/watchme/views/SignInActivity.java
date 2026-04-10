package kh.edu.rupp.watchme.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import kh.edu.rupp.watchme.R;

public class SignInActivity extends AppCompatActivity {
    TextView fgtPassword, signUp;
    MaterialButton btnSignIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_screen);

        fgtPassword = findViewById(R.id.tvForgotPassword);
        signUp = findViewById(R.id.tvSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);

        fgtPassword.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
        });

        signUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            finish();
        });

        btnSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, HomeActivity.class));
            finish();
        });
    }
}

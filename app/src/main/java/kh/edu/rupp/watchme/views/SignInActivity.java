package kh.edu.rupp.watchme.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.models.AuthResponse;
import kh.edu.rupp.watchme.viewmodels.AuthViewModel;

public class SignInActivity extends AppCompatActivity {
    TextView fgtPassword, signUp;
    MaterialButton btnSignIn;
    EditText etEmail, etPassword;
    AuthViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_screen);

        fgtPassword = findViewById(R.id.tvForgotPassword);
        signUp = findViewById(R.id.tvSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        fgtPassword.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
        });

        signUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });

        btnSignIn.setOnClickListener(v -> {
            String email = this.etEmail.getText().toString().trim();
            String password = this.etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            viewModel.signIn(email, password);
        });

        viewModel.getSignInResult().observe(this, response -> {
            if (response != null) {

                String accessToken = response.getAccess_token();
                String refreshToken = response.getRefresh_token();
                viewModel.saveSession(accessToken, refreshToken);

                startActivity(new Intent(this, HomeActivity.class));
                finish();
            }else {
                Toast.makeText(this, "SignIn failed", Toast.LENGTH_SHORT).show();
            }

        });
    }
}

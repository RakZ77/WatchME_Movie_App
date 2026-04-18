package kh.edu.rupp.watchme.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.utils.SimpleTextWatcher;
import kh.edu.rupp.watchme.viewmodels.AuthViewModel;

public class SignInActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private TextView tvEmailError, tvPasswordError;
    private TextView fgtPassword, signUp;
    private MaterialButton btnSignIn;
    private ImageButton btnTogglePassword;
    private AuthViewModel viewModel;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_screen);

        tvEmailError = findViewById(R.id.tvEmailError);
        tvPasswordError = findViewById(R.id.tvPasswordError);
        fgtPassword = findViewById(R.id.tvForgotPassword);
        signUp = findViewById(R.id.tvSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Clear errors as user types
        etEmail.addTextChangedListener(new SimpleTextWatcher(() -> hideError(tvEmailError)));
        etPassword.addTextChangedListener(new SimpleTextWatcher(() -> hideError(tvPasswordError)));

        // Toggle password visibility
        btnTogglePassword.setOnClickListener(v -> {
            isPasswordVisible = !isPasswordVisible;
            etPassword.setInputType(isPasswordVisible
                    ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etPassword.setSelection(etPassword.getText().length());
        });

        fgtPassword.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));
        });

        signUp.setOnClickListener(v -> {
            startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
        });

        btnSignIn.setOnClickListener(v -> {
            String email = this.etEmail.getText().toString().trim();
            String password = this.etPassword.getText().toString().trim();

            if (validateInputs(email, password)) {
                viewModel.signIn(email, password);
            }
        });

        viewModel.getSignInResult().observe(this, response -> {
            if (response != null) {
                viewModel.saveSession(
                    response.getAccess_token(),
                    response.getRefresh_token(),
                    response.getUser().getId()
                );

                startActivity(new Intent(this, MainActivity.class));
                finish();
            }else {
                showError(tvPasswordError, "Incorrect email or password");
            }

        });
    }

    private boolean validateInputs(String email, String password) {
        boolean valid = true;

        if(email.isEmpty()){
            showError(tvEmailError, "Email is required");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(tvEmailError, "Invalid email format");
            valid = false;
        }

        if (password.isEmpty()) {
            showError(tvPasswordError, "Password is required");
            valid = false;
        } else if (password.length() < 5) {
            showError(tvPasswordError, "Password must be at least 6 characters");
            valid = false;
        }
        return valid;
    }

    private void showError(TextView errorView, String message) {
        errorView.setText(message);
        errorView.setVisibility(View.VISIBLE);
    }

    private void hideError(TextView errorView) {
        errorView.setVisibility(View.GONE);
    }
}

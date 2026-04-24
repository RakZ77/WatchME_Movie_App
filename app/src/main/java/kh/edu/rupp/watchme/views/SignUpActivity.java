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

public class SignUpActivity extends AppCompatActivity {
    private TextView tvSignIn;
    private EditText etUsername, etEmail, etPassword;
    private TextView tvUsernameError, tvEmailError, tvPasswordError;
    private MaterialButton btnSignUp;
    private ImageButton btnTogglePassword;
    private AuthViewModel viewModel;
    private boolean isPasswordVisible = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        tvUsernameError = findViewById(R.id.tvUsernameError);
        tvEmailError = findViewById(R.id.tvEmailError);
        tvPasswordError = findViewById(R.id.tvPasswordError);
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnTogglePassword = findViewById(R.id.btnTogglePassword);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);


        tvSignIn = findViewById(R.id.tvSignIn);
        tvSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
        });

        // Clear errors as user types
        etUsername.addTextChangedListener(new SimpleTextWatcher(() -> hideError(tvUsernameError)));;
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

        btnSignUp.setOnClickListener(v -> {
            String userName = this.etUsername.getText().toString().trim();
            String email = this.etEmail.getText().toString().trim();
            String password = this.etPassword.getText().toString().trim();

            if(validateInputs(userName, email, password)){
                viewModel.signUp(userName, email, password);
            }
        });

        viewModel.getSignUpResult().observe(this, response -> {
            if (response != null) {
                viewModel.saveSession(
                        response.getAccess_token(),
                        response.getRefresh_token(),
                        response.getUser().getId()
                );

                startActivity(new Intent(this, MainActivity.class));
                finish();

            }else {
                showError(tvPasswordError, "Sign Up Failed");
            }
        });
    }

    private boolean validateInputs(String userName, String email, String password) {
        boolean valid = true;

        if(userName.isEmpty()){
            showError(tvUsernameError, "Username is required");
            valid = false;
        } else if (userName.length() < 4){
            showError(tvUsernameError, "Username must be at least 4 characters");
            valid = false;
        }

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
        } else if (password.length() < 6) {
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

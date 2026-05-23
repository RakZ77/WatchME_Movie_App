package kh.edu.rupp.watchme.views;

import android.content.Intent;
import android.net.Uri;
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
import kh.edu.rupp.watchme.utils.SimpleTextWatcher;
import kh.edu.rupp.watchme.viewmodels.AuthViewModel;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText etNewPassword, etConfirmPassword;
    private TextView tvNewPasswordError, tvConfirmPasswordError;
    private MaterialButton btnUpdatePassword;
    private AuthViewModel viewModel;
    private String accessToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tvNewPasswordError = findViewById(R.id.tvNewPasswordError);
        tvConfirmPasswordError = findViewById(R.id.tvConfirmPasswordError);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Disable button until token is verified
        btnUpdatePassword.setEnabled(false);

        extractToken();

        etNewPassword.addTextChangedListener(new SimpleTextWatcher(() -> hideError(tvNewPasswordError)));
        etConfirmPassword.addTextChangedListener(new SimpleTextWatcher(() -> hideError(tvConfirmPasswordError)));

        btnUpdatePassword.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (validateInputs(newPassword, confirmPassword)) {
                btnUpdatePassword.setEnabled(false);
                viewModel.updatePassword(accessToken, newPassword);
            }
        });

        // Observe token verification result
        viewModel.getVerifyOtpResult().observe(this, token -> {
            if (token != null) {
                accessToken = token;
                btnUpdatePassword.setEnabled(true);
            } else {
                Toast.makeText(this, "Link expired. Please request a new one.", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, ForgotPasswordActivity.class));
                finish();
            }
        });

        // Observe password update result
        viewModel.getUpdatePasswordResult().observe(this, success -> {
            btnUpdatePassword.setEnabled(true);
            if (success != null && success) {
                Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Failed to update password. Try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void extractToken() {
        Uri uri = getIntent().getData();
        android.util.Log.d("RESET", "Full URL: " + uri);

        if (uri == null) {
            Toast.makeText(this, "No reset link found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // NEW FLOW: token_hash in query param (prefetch-safe)
        // Link looks like: watchme://reset-password?token_hash=xxxx&type=recovery
        String tokenHash = uri.getQueryParameter("token_hash");
        String type = uri.getQueryParameter("type");

        android.util.Log.d("RESET", "token_hash: " + tokenHash);
        android.util.Log.d("RESET", "type: " + type);

        if (tokenHash != null && "recovery".equals(type)) {
            viewModel.verifyOtp(tokenHash, type);
            return;
        }

        // FALLBACK: old fragment flow (access_token in #fragment)
        // Link looks like: watchme://reset-password#access_token=xxx&type=recovery
        String fragment = uri.getFragment();
        android.util.Log.d("RESET", "Fragment: " + fragment);

        if (fragment != null && !fragment.contains("error=")) {
            accessToken = extractParam(fragment, "access_token");
            if (accessToken != null) {
                btnUpdatePassword.setEnabled(true);
                return;
            }
        }

        // Nothing worked
        Toast.makeText(this, "Invalid or expired link. Please request a new one.", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, ForgotPasswordActivity.class));
        finish();
    }

    private String extractParam(String source, String key) {
        for (String part : source.split("&")) {
            if (part.startsWith(key + "=")) {
                return part.substring(key.length() + 1);
            }
        }
        return null;
    }

    private boolean validateInputs(String password, String confirmPassword) {
        boolean valid = true;

        if (password.isEmpty()) {
            showError(tvNewPasswordError, "Password is required");
            valid = false;
        } else if (password.length() < 6) {
            showError(tvNewPasswordError, "Password must be at least 6 characters");
            valid = false;
        }

        if (confirmPassword.isEmpty()) {
            showError(tvConfirmPasswordError, "Please confirm your password");
            valid = false;
        } else if (!password.equals(confirmPassword)) {
            showError(tvConfirmPasswordError, "Passwords do not match");
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
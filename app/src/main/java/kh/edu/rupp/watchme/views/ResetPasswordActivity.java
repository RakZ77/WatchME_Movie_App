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

        extractToken();
        tvNewPasswordError.addTextChangedListener(new SimpleTextWatcher(()-> hideError(tvNewPasswordError)));
        etConfirmPassword.addTextChangedListener(new SimpleTextWatcher(()-> hideError(tvConfirmPasswordError)));

        btnUpdatePassword.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if(validateInputs(newPassword, confirmPassword)){
                btnUpdatePassword.setEnabled(false);
                viewModel.updatePassword(accessToken, newPassword);
            }
        });

        viewModel.getUpdatePasswordResult().observe(this, success  -> {
            btnUpdatePassword.setEnabled(true);

            if (success != null && success) {
                Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update password", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void extractToken(){
        Uri uri = getIntent().getData();

        if(uri != null && uri.getFragment() != null){
            accessToken = extractAccessToken(uri.getFragment());
        }

        if (accessToken == null) {
            showError(tvConfirmPasswordError, "Expired Token");
            finish();
        }
    }

    private String extractAccessToken(String fragment) {
        String[] parts = fragment.split("&");
        for (String part : parts) {
            if (part.startsWith("access_token=")) {
                return part.split("=")[1];
            }
        }
        return null;
    }

    private boolean validateInputs(String password, String confirmPassword) {
        boolean valid = true;

        if (password.length() < 6) {
            showError(tvNewPasswordError, "Password must be at least 6 characters");
            valid = false;
        }

        if (!password.equals(confirmPassword)) {
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

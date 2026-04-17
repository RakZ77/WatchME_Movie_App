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
import kh.edu.rupp.watchme.utils.SimpleTextWatcher;
import kh.edu.rupp.watchme.viewmodels.AuthViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText etEmail;
    private TextView tvEmailError;
    private MaterialButton btnResetPassword;
    private AuthViewModel viewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_screen);

        etEmail = findViewById(R.id.etEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        tvEmailError = findViewById(R.id.tvEmailError);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        etEmail.addTextChangedListener(new SimpleTextWatcher(() -> hideError(tvEmailError)));

        btnResetPassword.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if(validateInputs(email)){
                viewModel.forgotPassword(email);
            }
        });

        viewModel.getForgotResult().observe(this, success -> {
            if(success != null){
                tvEmailError.setVisibility(View.GONE);
                Toast.makeText(this,
                        "Reset link sent! Check your email.",
                        Toast.LENGTH_LONG).show();

                startActivity(new Intent(this, SignInActivity.class));
                finish();

            }else {
                showError(tvEmailError, "Invalid email");
            }
        });

    }

    private boolean validateInputs(String email) {
        boolean valid = true;

        if(email.isEmpty()){
            showError(tvEmailError, "Email is required");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(tvEmailError, "Invalid email format");
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

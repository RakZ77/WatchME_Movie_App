package kh.edu.rupp.watchme.views;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.models.FeedbackRequest;
import kh.edu.rupp.watchme.network.RetrofitClient;
import kh.edu.rupp.watchme.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etFeedback;
    private android.widget.Button btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        btnBack   = findViewById(R.id.btnBack);
        etFeedback = findViewById(R.id.etFeedback);
        btnSubmit  = findViewById(R.id.btnSubmit);

        btnBack.setOnClickListener(v -> finish());

        btnSubmit.setOnClickListener(v -> {
            String feedbackContent = etFeedback.getText().toString().trim();
            if (feedbackContent.isEmpty()) {
                Toast.makeText(this, "Please write something first!", Toast.LENGTH_SHORT).show();
            } else {
                sendFeedbackToSupabase(feedbackContent);
            }
        });
    }

    private void sendFeedbackToSupabase(String message) {
        SessionManager session = new SessionManager(this);
        String userId = session.getUserId(); // gets the logged-in user's UUID

        FeedbackRequest request = new FeedbackRequest(userId, message);

        RetrofitClient.getSupabaseService()
                .submitFeedback("return=minimal", request)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(FeedbackActivity.this, "Feedback submitted! Thank you 🙏", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(FeedbackActivity.this, "Failed: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(FeedbackActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
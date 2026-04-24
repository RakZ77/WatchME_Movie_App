package kh.edu.rupp.watchme.views;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.models.UpdateProfileRequest;
import kh.edu.rupp.watchme.utils.SessionManager;
import kh.edu.rupp.watchme.viewmodels.ProfileViewModel;

public class EditProfileActivity extends AppCompatActivity {
    private ImageView btnBack;
    private MaterialButton btnSave;
    private EditText etBirthday, etLocation, etUsername;
    private ShapeableImageView imgAvatar;
    private AutoCompleteTextView spinnerGender;
    private String avatarUrl;
    private Uri pendingImageUri = null;
    private String userId;

    private ProfileViewModel profileViewModel;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);
        etBirthday = findViewById(R.id.etBirthday);
        etUsername = findViewById(R.id.etUsername);
        etLocation = findViewById(R.id.etLocation);
        imgAvatar = findViewById(R.id.imgAvatar);
        spinnerGender = findViewById(R.id.spinnerGender);

        sessionManager = new SessionManager(this);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        userId = sessionManager.getUserId();

        btnBack.setOnClickListener(v -> finish());

        // Gender dropdown
        String[] genders = {"Male", "Female", "Prefer not to say"};
        spinnerGender.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, genders));

        // Birthday date picker
        etBirthday.setFocusable(false);
        etBirthday.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            String existing = etBirthday.getText().toString().trim();
            if (!existing.isEmpty()) {
                try {
                    String[] parts = existing.split("-");
                    cal.set(Integer.parseInt(parts[0]),
                            Integer.parseInt(parts[1]) - 1,
                            Integer.parseInt(parts[2]));
                } catch (Exception ignored) {}
            }
            new DatePickerDialog(this, (view, year, month, day) -> {
                etBirthday.setText(year + "-" + String.format("%02d", month + 1)
                        + "-" + String.format("%02d", day));
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Load current user profile
        etUsername.setText(getIntent().getStringExtra("username"));
        etBirthday.setText(getIntent().getStringExtra("birthday"));
        etLocation.setText(getIntent().getStringExtra("location"));
        spinnerGender.setText(getIntent().getStringExtra("gender"), false);
        avatarUrl = getIntent().getStringExtra("avatar_url"); // ✅ set avatarUrl here

        if (avatarUrl != null && !avatarUrl.isEmpty()) {
            Picasso.get()
                    .load(avatarUrl)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .placeholder(R.drawable.cat_profile)
                    .into(imgAvatar);
        }

        imgAvatar.setOnClickListener(v -> pickImage());

        btnSave.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String birthday = etBirthday.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String gender = spinnerGender.getText().toString().trim();

            if (username.isEmpty()) {
                etUsername.setError("Username is required");
                return;
            }

            btnSave.setEnabled(false);
            btnSave.setText("Saving...");

            if (pendingImageUri != null) {
                // Upload avatar first, then save will be triggered in getAvatarUploadResult observer
                profileViewModel.uploadAvatar(userId, pendingImageUri, this);
            } else {
                // No new image, save directly
                saveProfile(username, avatarUrl, birthday, gender, location);
            }
        });

        // Observe avatar upload result ONCE here
        profileViewModel.getAvatarUploadResult().observe(this, uploadResult -> {
            if ("success".equals(uploadResult)) {
                String uploadedUrl = profileViewModel.getPendingAvatarUrl();
                avatarUrl = uploadedUrl != null ? uploadedUrl : avatarUrl;

                // Now save the full profile with the new avatar URL
                saveProfile(
                        etUsername.getText().toString().trim(),
                        avatarUrl,
                        etBirthday.getText().toString().trim(),
                        spinnerGender.getText().toString().trim(),
                        etLocation.getText().toString().trim()
                );
            } else if ("failed".equals(uploadResult)) {
                btnSave.setEnabled(true);
                btnSave.setText("Save");
                Toast.makeText(this, "Avatar upload failed.", Toast.LENGTH_SHORT).show();
            }
        });

        // Observe update result ONCE
        profileViewModel.getUpdateResult().observe(this, success -> {
            if (success) {
                Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                btnSave.setEnabled(true);
                btnSave.setText("Save");
                Toast.makeText(this, "Update failed. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfile(String username, String avatarUrl,
                             String birthday, String gender, String location) {
        UpdateProfileRequest request = new UpdateProfileRequest(
                username, avatarUrl, birthday, gender, location);
        profileViewModel.updateUserProfile(userId, request);
    }

    private ActivityResultLauncher<Intent> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    pendingImageUri = result.getData().getData();
                    imgAvatar.setImageURI(pendingImageUri); // just preview, no upload
                }
            });

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }
}
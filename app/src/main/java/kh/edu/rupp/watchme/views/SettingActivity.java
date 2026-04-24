package kh.edu.rupp.watchme.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.models.Profiles;
import kh.edu.rupp.watchme.models.User;
import kh.edu.rupp.watchme.utils.SessionManager;
import kh.edu.rupp.watchme.viewmodels.ProfileViewModel;

public class SettingActivity extends AppCompatActivity {
    private ImageView btnBack;
    private ProfileViewModel profileViewModel;
    private SessionManager sessionManager;
    private TextView tvUsername, tvBirthday, tvContact, tvGender, tvLocation;
    private LinearLayout btnChangeProfile;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v->{
            finish();
        });

        tvUsername = findViewById(R.id.tvUsername);
        tvBirthday = findViewById(R.id.tvBirthday);
        tvContact = findViewById(R.id.tvContact);
        tvGender = findViewById(R.id.tvGender);
        tvLocation = findViewById(R.id.tvLocation);
        btnChangeProfile = findViewById(R.id.btnChangeProfile);

        sessionManager = new SessionManager(this);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        String userId = sessionManager.getUserId();
        String email = sessionManager.getEmail();

        profileViewModel.getUserProfile(userId);

        profileViewModel.getUserProfileLiveData().observe(this, profile -> {
            if (profile != null) {
                tvUsername.setText(profile.getUsername());
                tvContact.setText(email);
                tvBirthday.setText(profile.getBirthday());
                tvGender.setText(profile.getGender());
                tvLocation.setText(profile.getLocation());
            }else {
                tvGender.setText("No gender");
                tvLocation.setText("No location");
                tvBirthday.setText("No birthday");
                tvUsername.setText("No name");
                tvContact.setText("No contact");
            }

        });

        btnChangeProfile.setOnClickListener(v -> {
            Profiles profile = profileViewModel.getUserProfileLiveData().getValue();
            if (profile != null) {
                Intent intent = new Intent(this, EditProfileActivity.class);
                intent.putExtra("username", profile.getUsername());
                intent.putExtra("birthday", profile.getBirthday());
                intent.putExtra("gender", profile.getGender());
                intent.putExtra("location", profile.getLocation());
                intent.putExtra("avatar_url", profile.getAvatar_url());
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        profileViewModel.getUserProfile(sessionManager.getUserId());
    }
}

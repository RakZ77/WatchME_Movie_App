package kh.edu.rupp.watchme.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.utils.SessionManager;
import kh.edu.rupp.watchme.viewmodels.AuthViewModel;

public class ProfileFragment extends Fragment {
    private AuthViewModel viewModel;
    private TextView tvName;
    private SessionManager sessionManager;
    private MaterialButton btnLogout;
    private ShapeableImageView imgProfile;
    private LinearLayout settingAct, membershipAct, notificationAct, helpAct, aboutAct, feedbackAct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvName = view.findViewById(R.id.tvUserName);
        btnLogout = view.findViewById(R.id.btnLogout);
        imgProfile = view.findViewById(R.id.imgProfile);

        settingAct = view.findViewById(R.id.itemSettings);
        membershipAct = view.findViewById(R.id.itemMembership);
        notificationAct = view.findViewById(R.id.itemNotification);
        helpAct = view.findViewById(R.id.itemHelpCenter);
        aboutAct = view.findViewById(R.id.itemAbout);
        feedbackAct = view.findViewById(R.id.itemFeedback);

        sessionManager = new SessionManager(requireContext());
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        String userId = sessionManager.getUserId();
        String token = sessionManager.getAcessToken();
        viewModel.getUserProfile(userId, token);

        viewModel.getUserProfileLiveData().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                tvName.setText(profile.getUsername());

                String url = profile.getAvatar_url();
                if (url != null && !url.isEmpty()) {
                    Picasso.get()
                            .load(url)
                            .placeholder(R.drawable.cat_profile)
                            .error(R.drawable.cat_profile)
                            .into(imgProfile);
                } else {
                    imgProfile.setImageResource(R.drawable.cat_profile);
                }
            } else {
                tvName.setText("No name");
            }
        });

        btnLogout.setOnClickListener(v -> {
            viewModel.logout();

            Intent intent = new Intent(requireActivity(), SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        settingAct.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), SettingActivity.class));
        });
        aboutAct.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), AboutActivity.class));
        });
        membershipAct.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), MembershipActivity.class));
        });
        notificationAct.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), NotificationActivity.class));
        });
        feedbackAct.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), FeedbackActivity.class));
        });
        helpAct.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), HelpActivity.class));
        });

        return view;
    }
}

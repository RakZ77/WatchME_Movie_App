package kh.edu.rupp.watchme.views;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.adapters.ReviewAdapter;
import kh.edu.rupp.watchme.models.Profiles;
import kh.edu.rupp.watchme.models.Review;
import kh.edu.rupp.watchme.utils.SessionManager;
import kh.edu.rupp.watchme.utils.TokenRefreshHelper;
import kh.edu.rupp.watchme.viewmodels.ReviewViewModel;

public class ReviewFragment extends Fragment {
    private static final String ARG_MOVIE_ID = "movie_id";

    private ReviewViewModel viewModel;
    private ReviewAdapter adapter;
    private SessionManager sessionManager;

    private RecyclerView reviewRecyclerView;
    private EditText etComment;
    private ImageButton btnSubmit;
    private int movieId;

    public static ReviewFragment newInstance(int movieId) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        movieId = getArguments().getInt(ARG_MOVIE_ID);
        reviewRecyclerView = view.findViewById(R.id.reviewRecyclerView);
        etComment = view.findViewById(R.id.etComment);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        adapter = new ReviewAdapter();
        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        reviewRecyclerView.setAdapter(adapter);

        viewModel      = new ViewModelProvider(this).get(ReviewViewModel.class);
        sessionManager = new SessionManager(requireContext());

        viewModel.getReviews(movieId).observe(getViewLifecycleOwner(), reviews -> {
            adapter.setReviews(reviews);
        });

        btnSubmit.setOnClickListener(v -> {
            String comment = etComment.getText().toString().trim();
            if (TextUtils.isEmpty(comment)) {
                etComment.setError("Comment cannot be empty");
                return;
            }

            String userId = sessionManager.getUserId();
            String accessToken = sessionManager.getAccessToken();

            if (userId == null || accessToken == null) {
                Toast.makeText(requireContext(),
                        "Please sign in to leave a review", Toast.LENGTH_SHORT).show();
                return;
            }

            btnSubmit.setEnabled(false);

            TokenRefreshHelper.refresh(requireContext(), new TokenRefreshHelper.OnTokenRefreshed() {
                @Override
                public void onSuccess(String newToken) {
                    Review newReview = new Review(userId, movieId, comment);

                    viewModel.addReview(newReview).observe(getViewLifecycleOwner(), review -> {
                        btnSubmit.setEnabled(true);
                        if (review != null) {
                            Profiles localProfile = new Profiles(
                                    sessionManager.getUsername(),
                                    sessionManager.getAvatarUrl()
                            );
                            review.setProfiles(localProfile);

                            adapter.addReview(review);
                            reviewRecyclerView.scrollToPosition(0);
                            etComment.setText("");
                        } else {
                            Toast.makeText(requireContext(),
                                    "Failed to post review", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure() {
                    btnSubmit.setEnabled(true);
                    Toast.makeText(requireContext(),
                            "Session expired. Please sign in again.", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}

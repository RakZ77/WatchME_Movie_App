package kh.edu.rupp.watchme.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import kh.edu.rupp.watchme.models.Review;
import kh.edu.rupp.watchme.repositories.ReviewRepository;

public class ReviewViewModel extends ViewModel {
    private ReviewRepository repo;

    public ReviewViewModel() {
        repo = new ReviewRepository();
    }

    public LiveData<List<Review>> getReviews(int movieId) {
        return repo.getReviews(movieId);
    }

    public LiveData<Review> addReview(Review review) {
        return repo.addReview(review);
    }

}

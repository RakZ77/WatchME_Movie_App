package kh.edu.rupp.watchme.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import kh.edu.rupp.watchme.models.Review;
import kh.edu.rupp.watchme.models.ReviewRequest;
import kh.edu.rupp.watchme.network.RetrofitClient;
import kh.edu.rupp.watchme.network.SupabaseService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewRepository {
    private SupabaseService api;

    public ReviewRepository(){
        api = RetrofitClient.getSupabaseService();
    }

    public LiveData<List<Review>> getReviews(int movieId) {
        MutableLiveData<List<Review>> data = new MutableLiveData<>();

        api.getReviews("id,user_id,movie_id,review,created_at,profiles(username,avatar_url)", "eq." + movieId)
                .enqueue(new Callback<List<Review>>() {
                    @Override
                    public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            data.setValue(response.body());
                        } else {
                            try {
                                String errorBody = response.errorBody().string();
                                android.util.Log.e("ReviewRepo", "Error " + response.code() + ": " + errorBody);
                            } catch (Exception e) {
                                android.util.Log.e("ReviewRepo", "Unknown error: " + e.getMessage());
                            }
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Review>> call, Throwable t) {
                        android.util.Log.e("ReviewRepo", "Network failure: " + t.getMessage());
                        data.setValue(null);
                    }
                });
        return data;
    }

    public LiveData<Review> addReview(Review review) {
        MutableLiveData<Review> data = new MutableLiveData<>();

        ReviewRequest request = new ReviewRequest(
                review.getUserId(),
                review.getMovieId(),
                review.getComment()
        );

        api.addReviews("return=representation", request)
                .enqueue(new Callback<List<Review>>() {
                    @Override
                    public void onResponse(Call<List<Review>> call,
                                           Response<List<Review>> response) {
                        if (response.isSuccessful()
                                && response.body() != null
                                && !response.body().isEmpty()) {
                            data.setValue(response.body().get(0));
                        } else {
                            try {
                                String errorBody = response.errorBody() != null
                                        ? response.errorBody().string() : "null body";
                                android.util.Log.e("ReviewRepo",
                                        "addReview Error " + response.code() + ": " + errorBody);
                            } catch (Exception e) {
                                android.util.Log.e("ReviewRepo", "addReview parse error: " + e.getMessage());
                            }
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Review>> call, Throwable t) {
                        data.setValue(null);
                    }
                });

        return data;
    }
}

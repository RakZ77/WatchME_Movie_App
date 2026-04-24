package kh.edu.rupp.watchme.viewmodels;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import kh.edu.rupp.watchme.models.Profiles;
import kh.edu.rupp.watchme.models.UpdateProfileRequest;
import kh.edu.rupp.watchme.repositories.ProfileRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends AndroidViewModel {
    private ProfileRepository repo;
    private MutableLiveData<Profiles> userProfile = new MutableLiveData<>();
    private MutableLiveData<Boolean> updateResult = new MutableLiveData<>();
    private MutableLiveData<String> avatarUploadResult = new MutableLiveData<>();
    private String pendingAvatarUrl = null;

    public ProfileViewModel (Application application) {
        super(application);
        repo = new ProfileRepository(application.getApplicationContext());
    }

    // Get user profile
    public void getUserProfile(String userId){
        repo.getUserProfile(userId, new Callback<List<Profiles>>() {
            @Override
            public void onResponse(Call<List<Profiles>> call, Response<List<Profiles>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userProfile.postValue(response.body().get(0));
                }else {
                    try {
                        String err = response.errorBody() != null ? response.errorBody().string() : "empty body";
                        Log.e("PROFILE", "HTTP " + response.code() + ": " + err);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    userProfile.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Profiles>> call, Throwable t) {
                Log.e("PROFILE", t.getMessage());
                userProfile.postValue(null);
            }
        });

    }

    public LiveData<Profiles> getUserProfileLiveData() {
        return userProfile;
    }

    public void updateUserProfile(String userId, UpdateProfileRequest request){
        repo.updateUserProfile(userId, request, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    updateResult.postValue(true);
                }else {
                    Log.e("PROFILE_UPDATE", "FAILED: " + response.code());
                    updateResult.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("PROFILE_UPDATE", t.getMessage());
                updateResult.postValue(false);
            }
        });
    }

    public LiveData<Boolean> getUpdateResult() { return updateResult; }

    public void uploadAvatar(String userId, Uri imageUri, Context context) {
        repo.uploadAndUpdateAvatar(userId, imageUri, context, new ProfileRepository.OnAvatarUploaded() {
            @Override
            public void onSuccess(String publicUrl) {
                pendingAvatarUrl = publicUrl;
                avatarUploadResult.postValue("success");
            }
            @Override
            public void onFailure() {
                avatarUploadResult.postValue("failed");
            }
        });
    }
    public String getPendingAvatarUrl() { return pendingAvatarUrl; }

    public LiveData<String> getAvatarUploadResult() { return avatarUploadResult; }
}

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
import kh.edu.rupp.watchme.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileViewModel extends AndroidViewModel {
    private ProfileRepository repo;
    private SessionManager sessionManager;
    private MutableLiveData<Profiles> userProfile = new MutableLiveData<>();
    private MutableLiveData<Boolean> updateResult = new MutableLiveData<>();
    private MutableLiveData<String> avatarUploadResult = new MutableLiveData<>();
    private MutableLiveData<Boolean> deleteAvatarResult = new MutableLiveData<>();
    private String pendingAvatarUrl = null;

    public ProfileViewModel (Application application) {
        super(application);
        repo = new ProfileRepository(application.getApplicationContext());
        sessionManager = new SessionManager(application.getApplicationContext());
    }

    // Get user profile
    public void getUserProfile(String userId){
        repo.getUserProfile(userId, new Callback<List<Profiles>>() {
            @Override
            public void onResponse(Call<List<Profiles>> call, Response<List<Profiles>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Profiles profile = response.body().get(0);

                    sessionManager.saveProfile(
                            profile.getUsername(),
                            profile.getAvatar_url()
                    );

                    userProfile.postValue(profile);
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
                    sessionManager.saveProfile(
                            request.getUsername(),
                            request.getAvatarUrl() != null
                                    ? request.getAvatarUrl()
                                    : sessionManager.getAvatarUrl()
                    );
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

    // Pass oldAvatarUrl when uploading
    public void uploadAvatar(String userId, Uri imageUri, Context context, String oldAvatarUrl) {
        repo.uploadAndUpdateAvatar(userId, imageUri, context, oldAvatarUrl,
                new ProfileRepository.OnAvatarUploaded() {
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

    // Remove avatar (set to null in DB + delete from storage)
    public void removeAvatar(String userId, String currentAvatarUrl) {
        repo.deleteAvatar(currentAvatarUrl, new ProfileRepository.OnAvatarDeleted() {
            @Override
            public void onSuccess() {
                UpdateProfileRequest request = new UpdateProfileRequest(null, "", null, null, null);
                repo.updateUserProfile(userId, request, new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        deleteAvatarResult.postValue(response.isSuccessful());
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        deleteAvatarResult.postValue(false);
                    }
                });
            }
            @Override
            public void onFailure() {
                deleteAvatarResult.postValue(false);
            }
        });
    }

    public LiveData<Boolean> getDeleteAvatarResult() { return deleteAvatarResult; }
    public String getPendingAvatarUrl() { return pendingAvatarUrl; }

    public LiveData<String> getAvatarUploadResult() { return avatarUploadResult; }
}

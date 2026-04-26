package kh.edu.rupp.watchme.repositories;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import kh.edu.rupp.watchme.models.Profiles;
import kh.edu.rupp.watchme.models.UpdateProfileRequest;
import kh.edu.rupp.watchme.network.RetrofitClient;
import kh.edu.rupp.watchme.network.SupabaseService;
import kh.edu.rupp.watchme.utils.SessionManager;
import kh.edu.rupp.watchme.utils.TokenRefreshHelper;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    private SupabaseService api;
    private SessionManager session;
    private Context context;

    public ProfileRepository(Context context) {
        this.context = context;
        api = RetrofitClient.getSupabaseService();
        session = new SessionManager(context);
    }

    public void getUserProfile(String userId,  Callback<List<Profiles>> callback) {
        api.getProfiles("id,username,avatar_url,birthday,gender,location", "eq." + userId)
                .enqueue(new Callback<List<Profiles>>() {
                    @Override
                    public void onResponse(Call<List<Profiles>> outerCall, Response<List<Profiles>> response) {
                        if (response.code() == 401) {
                            TokenRefreshHelper.refresh(context, new TokenRefreshHelper.OnTokenRefreshed(){
                                @Override
                                public void onSuccess(String newToken) {
                                    api.getProfiles("id,username,avatar_url,birthday,gender,Location", "eq." + userId)
                                            .enqueue(callback);
                                }
                                @Override
                                public void onFailure() {
                                    callback.onResponse(outerCall, Response.success(null));
                                }
                            });
                        } else {
                            callback.onResponse(outerCall, response);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Profiles>> outerCall, Throwable t) {
                        callback.onFailure(outerCall, t);
                    }
                });
    }

    public void updateUserProfile(String userId, UpdateProfileRequest request, Callback<Void> callback) {
        api.updateProfile("return=representation","eq." + userId, request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> outerCall, Response<Void> response) {
                if (response.code() == 401) {
                    TokenRefreshHelper.refresh(context, new TokenRefreshHelper.OnTokenRefreshed() {
                        @Override
                        public void onSuccess(String newToken) {
                            api.updateProfile("return=representation","eq." + userId, request).enqueue(callback);
                        }

                        @Override
                        public void onFailure(){
                            callback.onResponse(outerCall, Response.success(null));
                        }
                    });
                }else {
                    callback.onResponse(outerCall, response);
                }
            }

            @Override
            public void onFailure(Call<Void> outerCall, Throwable t) {
                callback.onFailure(outerCall, t);
            }
        });

    }

    public interface OnAvatarUploaded {
        void onSuccess(String publicUrl);
        void onFailure();
    }

    public void uploadAndUpdateAvatar(String userId, Uri imageUri, Context context, OnAvatarUploaded callback) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = readBytes(inputStream);

            String contentType = context.getContentResolver().getType(imageUri);
            if (contentType == null) contentType = "image/jpeg";

            RequestBody requestBody = RequestBody.create(MediaType.parse(contentType), imageBytes);

            String fileName = userId + "_" + System.currentTimeMillis() + ".jpg";
            String publicUrl = "https://scnjqgmuxdkkicbvvuiz.supabase.co"
                    + "/storage/v1/object/public/avatars/" + fileName;

            api.uploadAvatar(contentType, fileName, requestBody).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        updateAvatarUrl(userId, publicUrl, new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> c, Response<Void> r) {
                                if (r.isSuccessful()) {
                                    callback.onSuccess(publicUrl);
                                } else {
                                    callback.onFailure();
                                }
                            }
                            @Override
                            public void onFailure(Call<Void> c, Throwable t) {
                                callback.onFailure();
                            }
                        });
                    } else {
                        try {
                            Log.e("AVATAR_UPLOAD", response.errorBody().string());
                        } catch (Exception e) { e.printStackTrace(); }
                        callback.onFailure();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("AVATAR_UPLOAD", t.getMessage());
                    callback.onFailure();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailure();
        }
    }

    // Save the public URL into profiles.avatar_url
    private void updateAvatarUrl(String userId, String url, Callback<Void> callback) {
        Log.d("AVATAR_URL", "userId: " + userId);
        Log.d("AVATAR_URL", "url: " + url);

        UpdateProfileRequest request = new UpdateProfileRequest(null, url, null, null, null);
        api.updateProfile("return=representation", "eq." + userId, request)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.d("AVATAR_URL", "Response code: " + response.code());
                        if (!response.isSuccessful()) {
                            try {
                                Log.e("AVATAR_URL", "Error: " + response.errorBody().string());
                            } catch (Exception e) { e.printStackTrace(); }
                        }
                        callback.onResponse(call, response);
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("AVATAR_URL", "Failure: " + t.getMessage());
                        callback.onFailure(call, t);
                    }
                });
    }

    // Helper to read bytes from InputStream
    private byte[] readBytes(InputStream inputStream) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(chunk)) != -1) {
            buffer.write(chunk, 0, bytesRead);
        }
        return buffer.toByteArray();
    }

}
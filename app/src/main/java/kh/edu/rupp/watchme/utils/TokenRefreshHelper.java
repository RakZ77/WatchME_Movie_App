package kh.edu.rupp.watchme.utils;

import android.content.Context;

import kh.edu.rupp.watchme.models.AuthResponse;
import kh.edu.rupp.watchme.repositories.AuthRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TokenRefreshHelper {

    public interface OnTokenRefreshed {
        void onSuccess(String newToken);
        void onFailure();
    }

    public static void refresh(Context context, OnTokenRefreshed listener) {
        AuthRepository authRepo = new AuthRepository(context);
        SessionManager session = new SessionManager(context);

        android.util.Log.d("TokenRefresh", "Starting refresh...");
        android.util.Log.d("TokenRefresh", "Refresh token: " + session.getRefreshToken());

        authRepo.refreshToken(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> res) {
                android.util.Log.d("TokenRefresh", "Response code: " + res.code());
                if (res.isSuccessful() && res.body() != null) {
                    AuthResponse auth = res.body();
                    android.util.Log.d("TokenRefresh", "New token: " + auth.getAccess_token());
                    session.saveSession(
                            auth.getAccess_token(),
                            auth.getRefresh_token(),
                            auth.getUser().getId()
                    );
                    listener.onSuccess(auth.getAccess_token());
                } else {
                    try {
                        String err = res.errorBody() != null ? res.errorBody().string() : "null";
                        android.util.Log.e("TokenRefresh", "Failed: " + res.code() + " " + err);
                    } catch (Exception e) {
                        android.util.Log.e("TokenRefresh", "Failed to read error: " + e.getMessage());
                    }
                    listener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                android.util.Log.e("TokenRefresh", "Network failure: " + t.getMessage());
                listener.onFailure();
            }
        });
    }
}
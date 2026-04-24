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

        authRepo.refreshToken(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> res) {
                if (res.isSuccessful() && res.body() != null) {
                    AuthResponse auth = res.body();
                    session.saveSession(
                            auth.getAccess_token(),
                            auth.getRefresh_token(),
                            auth.getUser().getId()
                    );
                    listener.onSuccess(auth.getAccess_token());
                } else {
                    listener.onFailure();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                listener.onFailure();
            }
        });
    }
}
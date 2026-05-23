package kh.edu.rupp.watchme.repositories;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import kh.edu.rupp.watchme.models.AuthResponse;
import kh.edu.rupp.watchme.models.ForgotRequest;
import kh.edu.rupp.watchme.models.Profiles;
import kh.edu.rupp.watchme.models.SignInRequest;
import kh.edu.rupp.watchme.models.SignUpRequest;
import kh.edu.rupp.watchme.network.RetrofitClient;
import kh.edu.rupp.watchme.network.SupabaseService;
import kh.edu.rupp.watchme.utils.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private SupabaseService api;
    private SessionManager session;

    public AuthRepository(Context context){
        api = RetrofitClient.getSupabaseService();
        session = new SessionManager(context);
    }

    public void signIn(String email, String password, Callback<AuthResponse> callback){
        api.signIn(new SignInRequest(email, password)).enqueue(callback);
    }
    public void signUp(String userName, String email, String password, Callback<AuthResponse> callback){
        api.signUp(new SignUpRequest(userName, email, password)).enqueue(callback);
    }
    public void forgotPassword(String email, Callback<Void> callback){
        api.forgotPassword("watchme://reset-password", new ForgotRequest(email)).enqueue(callback);
    }

    public void updatePassword(String token, String password, Callback<Void> callback) {
        Map<String, String> body = new HashMap<>();
        body.put("password", password);

        String bearer = "Bearer " + token;
        api.updatePassword(bearer, body).enqueue(callback);
    }

    public void saveSession(String accessToken, String refreshToken, String userId){
        session.saveSession(accessToken, refreshToken, userId);
    }
    public void logout(){
        session.logout();
    }
    public boolean isSignedIn(){
        return session.getAccessToken() != null;
    }

    public void refreshToken(Callback<AuthResponse> callback) {
        String refreshToken = session.getRefreshToken();
        Map<String, String> body = new HashMap<>();
        body.put("refresh_token", refreshToken);
        api.refreshToken(body).enqueue(callback);
    }

    public void verifyOtp(String tokenHash, String type,
                          Consumer<String> onSuccess, Runnable onError) {
        Map<String, String> body = new HashMap<>();
        body.put("token_hash", tokenHash);
        body.put("type", type);

        api.verifyOtp(body).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    onSuccess.accept(response.body().getAccess_token());
                } else {
                    try {
                        // Log the error to help debug
                        String err = response.errorBody() != null ? response.errorBody().string() : "null";
                        android.util.Log.e("VERIFY_OTP", "Error: " + err);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    onError.run();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                android.util.Log.e("VERIFY_OTP", "Failure: " + t.getMessage());
                onError.run();
            }
        });
    }

}

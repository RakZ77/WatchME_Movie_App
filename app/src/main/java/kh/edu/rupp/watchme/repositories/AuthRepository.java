package kh.edu.rupp.watchme.repositories;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kh.edu.rupp.watchme.models.AuthResponse;
import kh.edu.rupp.watchme.models.ForgotRequest;
import kh.edu.rupp.watchme.models.Profiles;
import kh.edu.rupp.watchme.models.SignInRequest;
import kh.edu.rupp.watchme.models.SignUpRequest;
import kh.edu.rupp.watchme.network.RetrofitClient;
import kh.edu.rupp.watchme.network.SupabaseService;
import kh.edu.rupp.watchme.utils.SessionManager;
import retrofit2.Callback;

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
        api.forgotPassword(new ForgotRequest(email)).enqueue(callback);
    }

    public void updatePassword(String token, String password, Callback<Void> callback) {
        Map<String, String> body = new HashMap<>();
        body.put("password", password);

        String bearer = "Bearer " + token;
        api.updatePassword(bearer, body).enqueue(callback);
    }

    public void getUserProfile(String userId, String token, Callback<List<Profiles>> callback){
        api.getProfiles("Bearer " + token, "id,username,avatar_url", "eq." + userId).enqueue(callback);
    }

    public void saveSession(String accessToken, String refreshToken, String userId){
        session.saveSession(accessToken, refreshToken, userId);
    }
    public void logout(){
        session.logout();
    }
    public boolean isSignedIn(){
        return session.getAcessToken() != null;
    }

}

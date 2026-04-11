package kh.edu.rupp.watchme.repositories;

import android.app.Application;
import android.content.Context;

import kh.edu.rupp.watchme.models.AuthResponse;
import kh.edu.rupp.watchme.models.ForgotRequest;
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
    public void saveSession(String accessToken, String refreshToken){
        session.saveSession(accessToken, refreshToken);
    }
    public void logout(){
        session.logout();
    }
    public boolean isSignedIn(){
        return session.getAcessToken() != null;
    }

}

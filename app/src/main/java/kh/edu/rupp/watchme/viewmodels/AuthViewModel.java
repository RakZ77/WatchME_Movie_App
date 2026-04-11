package kh.edu.rupp.watchme.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import kh.edu.rupp.watchme.models.AuthResponse;
import kh.edu.rupp.watchme.repositories.AuthRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository repo;
    private MutableLiveData<AuthResponse> signInResult = new MutableLiveData<>();
    private MutableLiveData<AuthResponse> signUpResult =  new MutableLiveData<>();
    private MutableLiveData<Boolean> forgotResult = new MutableLiveData<>();

    public AuthViewModel (Application application) {
        super(application);
        repo = new AuthRepository(application.getApplicationContext());
    }

    // Sign In
    public void signIn(String email, String password){
        repo.signIn(email, password, new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    AuthResponse auth = response.body();
                    repo.saveSession(auth.getAccess_token(), auth.getRefresh_token());
                    signInResult.postValue(auth);
                }else {
                    signInResult.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                signInResult.postValue(null);
            }
        });
    }
    public LiveData<AuthResponse> getSignInResult() {
        return signInResult;
    }

    // Sign Up
    public void signUp(String userName, String email, String password){
        repo.signUp(userName, email, password, new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    signUpResult.postValue(response.body());
                } else {
                    signUpResult.postValue(null);
                }

            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                signUpResult.postValue(null);
            }
        });
    }
    public LiveData<AuthResponse> getSignUpResult() {
        return signUpResult;
    }

    // Forgot Password
    public void forgotPassword(String email){
        repo.forgotPassword(email, new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                forgotResult.postValue(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                forgotResult.postValue(false);
            }
        });
    }
    public LiveData<Boolean> getForgotResult() {
        return forgotResult;
    }

    // Session
    public void saveSession(String accessToken, String refreshToken){
        repo.saveSession(accessToken, refreshToken);
    }
    public void logout(){
        repo.logout();
    }
    public boolean isSignedIn(){
        return repo.isSignedIn();
    }


}

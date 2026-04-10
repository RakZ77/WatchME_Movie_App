package kh.edu.rupp.watchme.network;

import kh.edu.rupp.watchme.models.AuthResponse;
import kh.edu.rupp.watchme.models.ForgotRequest;
import kh.edu.rupp.watchme.models.SignInRequest;
import kh.edu.rupp.watchme.models.SignUpRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface SupabaseService {
    @POST("auth/v1/token?grant_type=password")
    Call<AuthResponse> login(@Body SignInRequest request);

    @POST("auth/v1/recover")
    Call<Void> forgotPassword(@Body ForgotRequest request);

    @POST("auth/v1/signup")
    Call<AuthResponse> signUp(@Body SignUpRequest request);
}

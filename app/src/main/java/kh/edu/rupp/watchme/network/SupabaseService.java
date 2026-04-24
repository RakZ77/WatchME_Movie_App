package kh.edu.rupp.watchme.network;

import java.util.List;
import java.util.Map;

import kh.edu.rupp.watchme.models.AuthResponse;
import kh.edu.rupp.watchme.models.ForgotRequest;
import kh.edu.rupp.watchme.models.Profiles;
import kh.edu.rupp.watchme.models.SignInRequest;
import kh.edu.rupp.watchme.models.SignUpRequest;
import kh.edu.rupp.watchme.models.UpdateProfileRequest;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SupabaseService {
    @POST("auth/v1/token?grant_type=password")
    Call<AuthResponse> signIn(@Body SignInRequest request);

    @POST("auth/v1/recover")
    Call<Void> forgotPassword(@Body ForgotRequest request);

    @POST("auth/v1/signup")
    Call<AuthResponse> signUp(@Body SignUpRequest request);

    @PUT("auth/v1/user")
    Call<Void> updatePassword(
            @Header("Authorization") String token,
            @Body Map<String, String> body
    );

    @GET("rest/v1/profiles")
    Call<List<Profiles>> getProfiles(
            @Query("select") String select,
            @Query("id") String userId
    );

    @POST("auth/v1/token?grant_type=refresh_token")
    Call<AuthResponse> refreshToken(@Body Map<String, String> body);

    @PATCH("rest/v1/profiles")
    Call<Void> updateProfile(
            @Header("Prefer") String prefer,
            @Query("id") String userId,
            @Body UpdateProfileRequest body
    );

    @PUT ("storage/v1/object/avatars/{filename}")
    Call<Void> uploadAvatar(
            @Header("Content-Type") String contentType,
            @Path("filename") String filename,
            @Body RequestBody image
    );
}

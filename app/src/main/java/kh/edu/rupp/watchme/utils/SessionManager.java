package kh.edu.rupp.watchme.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
    }
    public void saveSession(String accessToken, String refreshToken, String userId){
        prefs.edit().putString("access_token", accessToken)
                .putString("refresh_token", refreshToken)
                .putString("user_id", userId)
                .apply();
    }

    public String getAccessToken(){
        return prefs.getString("access_token", null);
    }

    public String getRefreshToken(){
        return prefs.getString("refresh_token", null);
    }

    public String getUserId(){
        return prefs.getString("user_id", null);
    }

    public void logout(){
        prefs.edit().clear().apply();
    }

    public void saveEmail(String email){
        prefs.edit().putString("email", email).apply();
    }

    public String getEmail(){
        return prefs.getString("email", null);
    }


}

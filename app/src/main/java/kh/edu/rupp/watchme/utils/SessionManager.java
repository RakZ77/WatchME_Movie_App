package kh.edu.rupp.watchme.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
    }
    public void saveSession(String accessToken, String refreshToken){
        prefs.edit().putString("access_token", accessToken).putString("refresh_token", refreshToken).apply();
    }

    public String getAcessToken(){
        return prefs.getString("access_token", null);
    }

    public String getRefreshToken(){
        return prefs.getString("refresh_token", null);
    }

    public void logout(){
        prefs.edit().clear().apply();
    }

}

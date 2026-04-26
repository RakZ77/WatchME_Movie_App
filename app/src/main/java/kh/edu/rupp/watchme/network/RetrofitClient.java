package kh.edu.rupp.watchme.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kh.edu.rupp.watchme.BuildConfig;
import kh.edu.rupp.watchme.utils.SessionManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://scnjqgmuxdkkicbvvuiz.supabase.co/";
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/";

    private static Retrofit retrofit;
    private static Retrofit tmdbRetrofit;
    private static SessionManager sessionManager;

    public static void init(Context context){
        sessionManager = new SessionManager(context);
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        String token = sessionManager != null ? sessionManager.getAccessToken() : null;
                        Request original = chain.request();

                        Request.Builder builder = chain.request().newBuilder()
                                .addHeader("apikey", BuildConfig.SUPABASE_ANON_KEY);

                        if(original.header("Content-Type") == null){
                            builder.addHeader("Content-Type", "application/json");
                        }

                        if (token != null) {
                            builder.addHeader("Authorization", "Bearer " + token);
                        }

                        return chain.proceed(builder.build());
                    }).build();

            Gson gson = new GsonBuilder().create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
    public static SupabaseService getSupabaseService(){
        return getClient().create(SupabaseService.class);
    }

    public static Retrofit getTMDbClient() {
        if (tmdbRetrofit == null) {
            tmdbRetrofit = new Retrofit.Builder()
                    .baseUrl(TMDB_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return tmdbRetrofit;
    }

    public static TMDbService getTMDbService() {
        return getTMDbClient().create(TMDbService.class);
    }
}

package kh.edu.rupp.watchme.network;

import kh.edu.rupp.watchme.BuildConfig;
import kh.edu.rupp.watchme.utils.SessionManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://scnjqgmuxdkkicbvvuiz.supabase.co/";
    private static Retrofit retrofit;
    private static SessionManager sessionManager;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        String token = sessionManager != null ? sessionManager.getAcessToken() : null;

                        Request.Builder builder = chain.request().newBuilder()
                                .addHeader("apikey", BuildConfig.SUPABASE_ANON_KEY)
                                .addHeader("Content-Type", "application/json");

                        if (token != null) {
                            builder.addHeader("Authorization", "Bearer " + token);
                        }

                        return chain.proceed(builder.build());
                    }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static SupabaseService getSupabaseService(){
        return getClient().create(SupabaseService.class);
    }
}

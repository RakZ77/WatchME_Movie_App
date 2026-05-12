package kh.edu.rupp.watchme;

import android.app.Application;
import kh.edu.rupp.watchme.network.RetrofitClient;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitClient.init(this);
    }
}
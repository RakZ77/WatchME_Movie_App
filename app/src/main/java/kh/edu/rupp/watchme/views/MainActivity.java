package kh.edu.rupp.watchme.views;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.network.RetrofitClient;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RetrofitClient.init(this);

        if (savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
        }

        BottomNavigationView navView = findViewById(R.id.bottomNav);
        navView.setOnItemSelectedListener(item -> {
            Fragment fragment = null;

            if (item.getItemId() == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_search) {
                fragment = new SearchFragment();
            } else if (item.getItemId() == R.id.nav_watchlist) {
                fragment = new WatchListFragment();
            } else if (item.getItemId() == R.id.nav_profile) {
                fragment = new ProfileFragment();
            } else {
                return false;
            }

            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }

            return true;
        });
    }
}

package kh.edu.rupp.watchme.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import kh.edu.rupp.watchme.views.AboutMovieFragment;
import kh.edu.rupp.watchme.views.CastFragment;
import kh.edu.rupp.watchme.views.ReviewFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private int movieId;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, int movieId) {
        super(fragmentActivity);
        this.movieId = movieId;
    }

    // Create fragment based on position
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new AboutMovieFragment().newInstance(movieId);
            case 1:
                return new ReviewFragment().newInstance(movieId);
            case 2:
                return new CastFragment().newInstance(movieId);
            default:
                return new AboutMovieFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

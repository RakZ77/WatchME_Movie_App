package kh.edu.rupp.watchme.views;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.viewmodels.MovieViewModel;

public class AboutMovieFragment extends Fragment {
    private static final String ARG_MOVIE_ID = "movie_id";

    public static AboutMovieFragment newInstance(int movieId){
        AboutMovieFragment fragment = new AboutMovieFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        int movieId = getArguments().getInt(ARG_MOVIE_ID);

        TextView tvOverview = view.findViewById(R.id.tvOverview);
        MovieViewModel viewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);

        viewModel.getMovieDetails(movieId).observe(getViewLifecycleOwner(), movie -> {
            if (movie != null) {
                tvOverview.setText(movie.getOverview());
            }
        });
    }
}

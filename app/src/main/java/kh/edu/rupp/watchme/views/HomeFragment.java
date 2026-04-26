package kh.edu.rupp.watchme.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.adapters.MovieAdapter;
import kh.edu.rupp.watchme.models.Movie;
import kh.edu.rupp.watchme.viewmodels.MovieViewModel;

public class HomeFragment extends Fragment {

    private MovieViewModel viewModel;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> movieList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFeaturedMovie);

        adapter = new MovieAdapter(movieList, movie -> {
            Intent intent = new Intent(requireContext(), DetailsAboutMovieActivity.class);
            intent.putExtra("movie", movie);
            startActivity(intent);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        viewModel.getMovies().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                movieList.clear();
                movieList.addAll(movies);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}


package kh.edu.rupp.watchme.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.adapters.WatchlistAdapter;
import kh.edu.rupp.watchme.models.Movie;
import kh.edu.rupp.watchme.models.Watchlist;
import kh.edu.rupp.watchme.viewmodels.MovieViewModel;

public class WatchListFragment extends Fragment {
    private WatchlistAdapter adapter;
    private List<Watchlist> watchlist = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_watch_list, container, false);

        MovieViewModel viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewWatchList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new WatchlistAdapter(watchlist, this::navigateToDetails);
        recyclerView.setAdapter(adapter);

        viewModel.getWatchlist().observe(getViewLifecycleOwner(), watchlist -> {
            adapter.setData(watchlist);
        });

        return view;
    }

    private void navigateToDetails(Watchlist movie) {
        Intent intent = new Intent(requireContext(), DetailsAboutMovieActivity.class);
        intent.putExtra("movie_id", movie.getId());
        startActivity(intent);
    }
}

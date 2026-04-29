package kh.edu.rupp.watchme.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.adapters.SearchAdapter;
import kh.edu.rupp.watchme.models.Movie;
import kh.edu.rupp.watchme.viewmodels.MovieViewModel;

public class SearchFragment extends Fragment {
    private EditText etSearch;
    private ImageView btnSearch;
    private MovieViewModel viewModel;
    private SearchAdapter adapter;
    private RecyclerView recyclerView;
    private int currentPage = 1;
    private List<Movie> searchAllList = new ArrayList<>();
    private boolean isLoading = false;
    private String currentQuery = "";
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        etSearch = view.findViewById(R.id.searchEditText);
        btnSearch = view.findViewById(R.id.btnSearch);
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        recyclerView = view.findViewById(R.id.all_movies_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        adapter = new SearchAdapter(searchAllList, this::navigateToDetails, viewModel, getViewLifecycleOwner());
        recyclerView.setAdapter(adapter);

        loadMovies("discover", 1, true);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cancel any pending search
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                searchRunnable = () -> {
                    String query = s.toString().trim();
                    if (!query.equals(currentQuery)) {
                        currentQuery = query;
                        currentPage = 1;

                        // Use "search" type if there's a query, otherwise show discover
                        String type = query.isEmpty() ? "discover" : "search";
                        loadMovies(type, currentPage, true);
                    }
                };

                // Wait 500ms after user stops typing
                searchHandler.postDelayed(searchRunnable, 500);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnSearch.setOnClickListener(v -> {
            if (searchRunnable != null) searchHandler.removeCallbacks(searchRunnable);
            String query = etSearch.getText().toString().trim();
            currentQuery = query;
            currentPage = 1;
            String type = query.isEmpty() ? "discover" : "search";
            loadMovies(type, currentPage, true);
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1) && !isLoading) {
                    isLoading = true;
                    currentPage++;
                    loadMore();
                }
            }
        });

        return view;
    }

    private void loadMovies(String type, int page, boolean reset) {
        if (type.equals("search")) {
            viewModel.searchMovies(currentQuery, page)
                    .observe(getViewLifecycleOwner(), movies -> handleResult(movies, reset));
        } else {
            viewModel.getMovies("discover", page)
                    .observe(getViewLifecycleOwner(), movies -> handleResult(movies, reset));
        }
    }

    private void handleResult(List<Movie> movies, boolean reset) {
        if (movies != null) {
            if (reset) {
                searchAllList.clear();
                searchAllList.addAll(movies);
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(0);
            } else {
                int start = searchAllList.size();
                searchAllList.addAll(movies);
                adapter.notifyItemRangeInserted(start, movies.size());
            }
        }
        isLoading = false;
    }


    private void navigateToDetails(Movie movie) {
        Intent intent = new Intent(requireContext(), DetailsAboutMovieActivity.class);
        intent.putExtra("movie_id", movie.getId());
        startActivity(intent);
    }

    private void loadMore() {
        String type = currentQuery.isEmpty() ? "discover" : "search";
        loadMovies(type, currentPage, false);
    }
}

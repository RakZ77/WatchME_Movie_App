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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.adapters.CategoryMovieAdapter;
import kh.edu.rupp.watchme.adapters.FeaturedMovieAdapter;
import kh.edu.rupp.watchme.adapters.SectionAdapter;
import kh.edu.rupp.watchme.models.Movie;
import kh.edu.rupp.watchme.models.MovieSection;
import kh.edu.rupp.watchme.viewmodels.MovieViewModel;

public class HomeFragment extends Fragment {

    private MovieViewModel viewModel;
    private RecyclerView feauredRecyclerView, catergoryRecyclerView;
    private FeaturedMovieAdapter featuredAdapter;

    private SectionAdapter sectionAdapter;
    private List<Movie> featuredList = new ArrayList<>();

    private List<Movie> popularMovies = new ArrayList<>();
    private List<Movie> topRatedMovies = new ArrayList<>();
    private List<Movie> upcomingMovies = new ArrayList<>();
    private List<Movie> nowPlayingMovies = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // Featured Movies
        feauredRecyclerView = view.findViewById(R.id.recyclerViewFeaturedMovie);
        featuredAdapter = new FeaturedMovieAdapter(featuredList, this::navigateToDetails);
        feauredRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        feauredRecyclerView.setAdapter(featuredAdapter);

        // Section Movies
        catergoryRecyclerView = view.findViewById(R.id.recyclerViewCategory);
        sectionAdapter = new SectionAdapter(this::navigateToDetails, viewModel, getViewLifecycleOwner());
        catergoryRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        catergoryRecyclerView.setAdapter(sectionAdapter);

        viewModel.getMovies("popular").observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                featuredList.clear();
                featuredList.addAll(movies);
                featuredAdapter.notifyDataSetChanged();

                popularMovies.clear();
                popularMovies.addAll(movies);
                rebuildSections();
            }
        });

        viewModel.getMovies("top_rated").observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                topRatedMovies.clear();
                topRatedMovies.addAll(movies);
                rebuildSections();
            }
        });

        viewModel.getMovies("upcoming").observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                upcomingMovies.clear();
                upcomingMovies.addAll(movies);
                rebuildSections();
            }
        });

        viewModel.getMovies("now_playing").observe(getViewLifecycleOwner(), movies -> {
            if (movies != null) {
                nowPlayingMovies.clear();
                nowPlayingMovies.addAll(movies);
                rebuildSections();
            }
        });

        return view;
    }

    private void rebuildSections(){
        List<MovieSection> sections = new ArrayList<>();

        if(!popularMovies.isEmpty()){
            sections.add(new MovieSection("Popular", popularMovies));
        }
        if(!topRatedMovies.isEmpty()){
            sections.add(new MovieSection("Top Rated", topRatedMovies));
        }
        if(!upcomingMovies.isEmpty()){
            sections.add(new MovieSection("Upcoming", upcomingMovies));
        }
        if(!nowPlayingMovies.isEmpty()){
            sections.add(new MovieSection("Now Playing", nowPlayingMovies));
        }

        sectionAdapter.setSections(sections);
    }

    private void navigateToDetails(Movie movie) {
        Intent intent = new Intent(requireContext(), DetailsAboutMovieActivity.class);
        intent.putExtra("movie_id", movie.getId());
        startActivity(intent);
    }
}


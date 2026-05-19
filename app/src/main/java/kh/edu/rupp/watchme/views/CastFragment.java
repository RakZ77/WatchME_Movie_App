package kh.edu.rupp.watchme.views;

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

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.adapters.CastAdapter;
import kh.edu.rupp.watchme.models.Cast;
import kh.edu.rupp.watchme.viewmodels.MovieViewModel;

public class CastFragment extends Fragment {
    private static final String ARG_MOVIE_ID = "movie_id";

    public static CastFragment newInstance(int movieId) {
        CastFragment fragment = new CastFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MOVIE_ID, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cast, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        int movieId = getArguments().getInt(ARG_MOVIE_ID);

        RecyclerView recyclerView = view.findViewById(R.id.castRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        CastAdapter adapter = new CastAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        MovieViewModel viewModel = new ViewModelProvider(requireActivity()).get(MovieViewModel.class);

        viewModel.getMovieCast(movieId).observe(getViewLifecycleOwner(), castList -> {
            adapter.setData(castList);
        });
    }
}

package kh.edu.rupp.watchme.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import kh.edu.rupp.watchme.R;
import kh.edu.rupp.watchme.models.MovieSection;
import kh.edu.rupp.watchme.viewmodels.MovieViewModel;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {
    private List<MovieSection> sections = new ArrayList<>();
    private CategoryMovieAdapter.OnMovieClickListener listener;
    private MovieViewModel viewModel;
    private LifecycleOwner lifecycleOwner;

    public SectionAdapter(CategoryMovieAdapter.OnMovieClickListener listener, MovieViewModel viewModel, LifecycleOwner lifecycleOwner) {
        this.listener = listener;
        this.lifecycleOwner = lifecycleOwner;
        this.viewModel = viewModel;
    }

    // Call this from fragment to update sections
    public void setSections(List<MovieSection> sections) {
        this.sections = sections;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        holder.bind(sections.get(position));
    }

    @Override
    public int getItemCount() { return sections.size(); }

    class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView tvSectionTitle;
        RecyclerView horizontalRecyclerView;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSectionTitle = itemView.findViewById(R.id.tvSectionTitle);
            horizontalRecyclerView = itemView.findViewById(R.id.horizontalRecyclerView);
            horizontalRecyclerView.setLayoutManager(
                    new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false)
            );
        }

        public void bind(MovieSection section) {
            tvSectionTitle.setText(section.getTitle());
            CategoryMovieAdapter adapter = new CategoryMovieAdapter(
                    section.getMovies(), listener, viewModel, lifecycleOwner
            );
            horizontalRecyclerView.setAdapter(adapter);
        }
    }
}

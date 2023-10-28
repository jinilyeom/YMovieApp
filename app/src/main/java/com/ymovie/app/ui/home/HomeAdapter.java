package com.ymovie.app.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.ymovie.app.R;
import com.ymovie.app.data.model.movie.Movie;
import com.ymovie.app.databinding.ItemHomeBinding;
import com.ymovie.app.network.NetworkConstants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private final Context context;
    private List<Movie> movies;

    public HomeAdapter(Context ctx) {
        context = ctx;
        movies = new ArrayList<>();
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeBinding binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new HomeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        ItemHomeBinding binding = holder.getItemHomeBinding();

        Glide.with(binding.imvBackdrop)
                .load(NetworkConstants.IMAGE_BASE_URL_W500 + movies.get(position).getBackdropPath())
                .into(binding.imvBackdrop);
        binding.tvTitle.setText(movies.get(position).getOriginalTitle());
        binding.tvReleaseDate.setText(movies.get(position).getReleaseDate());
        binding.tvVoteAverage.setText(context.getString(R.string.label_user_score, movies.get(position).getVoteAverage()));
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    public void addItemToList(List<Movie> list) {
        int position = movies.isEmpty() ? 0 : movies.size();

        movies.addAll(list);

        notifyItemInserted(position);
    }

    public static class HomeViewHolder extends RecyclerView.ViewHolder {
        private final ItemHomeBinding binding;

        public HomeViewHolder(ItemHomeBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ItemHomeBinding getItemHomeBinding() {
            return binding;
        }
    }
}

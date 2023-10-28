package com.ymovie.app.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.ymovie.app.R;
import com.ymovie.app.data.model.movie.Movie;
import com.ymovie.app.databinding.ItemSearchBinding;
import com.ymovie.app.network.NetworkConstants;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    private final Context context;
    private List<Movie> movies;

    public SearchAdapter(Context ctx) {
        context = ctx;
        movies = new ArrayList<>();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchBinding binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        ItemSearchBinding binding = holder.getItemSearchBinding();

        Glide.with(binding.imvThumbnail)
                .load(NetworkConstants.IMAGE_BASE_URL_W500 + movies.get(position).getPosterPath())
                .into(binding.imvThumbnail);
        binding.tvTitle.setText(movies.get(position).getOriginalTitle());
        binding.tvReleaseDate.setText(movies.get(position).getReleaseDate());
        binding.tvVoteAverage.setText(context.getString(R.string.label_user_score, movies.get(position).getVoteAverage()));
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    public void setItemToList(List<Movie> list) {
        if (!movies.isEmpty()) {
            movies.clear();
        }

        movies.addAll(list);

        notifyDataSetChanged();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        private final ItemSearchBinding binding;

        public SearchViewHolder(ItemSearchBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public ItemSearchBinding getItemSearchBinding() {
            return binding;
        }
    }
}

package com.ymovie.app.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ymovie.app.R
import com.ymovie.app.data.model.movie.Movie
import com.ymovie.app.databinding.ItemSearchBinding
import com.ymovie.app.network.NetworkConstants
import com.ymovie.app.ui.search.SearchAdapter.SearchViewHolder

class SearchAdapter(
    private val context: Context,
    private var movies: MutableList<Movie>
) : RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(context, movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setItemToList(list: List<Movie>) {
        if (movies.isNotEmpty()) {
            movies.clear()
        }

        movies.addAll(list)

        notifyDataSetChanged()
    }

    class SearchViewHolder(
        private val binding: ItemSearchBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, movie: Movie) {
            Glide.with(binding.imvThumbnail)
                .load(NetworkConstants.IMAGE_BASE_URL_W500 + movie.posterPath)
                .into(binding.imvThumbnail)
            binding.tvTitle.text = movie.originalTitle
            binding.tvReleaseDate.text = movie.releaseDate
            binding.tvVoteAverage.text = context.getString(R.string.label_user_score, movie.voteAverage)
        }
    }
}
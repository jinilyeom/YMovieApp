package com.ymovie.app.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ymovie.app.R
import com.ymovie.app.data.model.movie.Movie
import com.ymovie.app.databinding.ItemHomeBinding
import com.ymovie.app.network.NetworkConstants
import com.ymovie.app.ui.home.HomeAdapter.HomeViewHolder

class HomeAdapter(
    private val context: Context,
    private var movies: MutableList<Movie>
) : RecyclerView.Adapter<HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(context, movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun addItemToList(list: List<Movie>) {
        val position = if (movies.isEmpty()) 0 else movies.size

        movies.addAll(list)

        notifyItemInserted(position)
    }

    class HomeViewHolder(
        private val binding: ItemHomeBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, movie: Movie) {
            Glide.with(binding.imvBackdrop)
                .load(NetworkConstants.IMAGE_BASE_URL_W500 + movie.backdropPath)
                .into(binding.imvBackdrop)
            binding.tvTitle.text = movie.originalTitle
            binding.tvReleaseDate.text = movie.releaseDate
            binding.tvVoteAverage.text = context.getString(R.string.label_user_score, movie.voteAverage)
        }
    }
}
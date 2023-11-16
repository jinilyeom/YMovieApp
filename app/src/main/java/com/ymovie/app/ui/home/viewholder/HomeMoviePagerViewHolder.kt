package com.ymovie.app.ui.home.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ymovie.app.R
import com.ymovie.app.data.model.movie.Movie
import com.ymovie.app.databinding.ItemHomeMoviePagerBinding
import com.ymovie.app.network.NetworkConstants

class HomeMoviePagerViewHolder(
    private val binding: ItemHomeMoviePagerBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(context: Context, movie: Movie) {
        Glide.with(binding.imvBackdrop)
            .load(NetworkConstants.IMAGE_BASE_URL_W500 + movie.backdropPath)
            .into(binding.imvBackdrop)
        Glide.with(binding.imvPoster)
            .load(NetworkConstants.IMAGE_BASE_URL_W200 + movie.posterPath)
            .into(binding.imvPoster)
        binding.tvTitle.text = movie.originalTitle
        binding.tvReleaseDate.text = movie.releaseDate
        binding.tvVoteAverage.text =
            String.format(
                context.getString(R.string.label_number_with_percent),
                (movie.voteAverage * 10).toInt()
            )
    }
}
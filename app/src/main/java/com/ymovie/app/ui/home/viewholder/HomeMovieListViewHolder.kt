package com.ymovie.app.ui.home.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ymovie.app.R
import com.ymovie.app.data.model.movie.Movie
import com.ymovie.app.databinding.ItemHomeMovieListBinding
import com.ymovie.app.network.NetworkConstants

class HomeMovieListViewHolder(
    private val binding: ItemHomeMovieListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(context: Context, movie: Movie) {
        Glide.with(binding.imvPoster)
            .load(NetworkConstants.IMAGE_BASE_URL_W200 + movie.posterPath)
            .into(binding.imvPoster)

        binding.pbVoteAverage.let {
            it.setTrackColor(
                (movie.voteAverage * 10).toInt().let { percent ->
                    if (percent >= 70) {
                        context.getColor(R.color.user_score_track)
                    } else {
                        context.getColor(R.color.user_score_track_under_70)
                    }
                }
            )
            it.setIndicatorColor(
                (movie.voteAverage * 10).toInt().let { percent ->
                    if (percent >= 70) {
                        context.getColor(R.color.user_score_indicator)
                    } else {
                        context.getColor(R.color.user_score_indicator_under_70)
                    }
                }
            )
            it.setProgress((movie.voteAverage * 10).toInt())
            it.setLabelText(
                String.format(
                    context.getString(R.string.label_number_with_percent),
                    (movie.voteAverage * 10).toInt()
                )
            )
        }

        binding.tvTitle.text = movie.originalTitle
        binding.tvReleaseDate.text = movie.releaseDate
    }
}
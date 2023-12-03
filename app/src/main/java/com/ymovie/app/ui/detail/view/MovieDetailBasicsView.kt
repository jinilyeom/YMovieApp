package com.ymovie.app.ui.detail.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.ymovie.app.R
import com.ymovie.app.data.model.movie.MovieDetail
import com.ymovie.app.databinding.ViewMovieDetailBasicBinding

class MovieDetailBasicsView(
    private val context: Context,
    private val attributeSet: AttributeSet
) : ConstraintLayout(context, attributeSet) {

    private val binding: ViewMovieDetailBasicBinding by lazy {
        ViewMovieDetailBasicBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.view_movie_detail_basic, this, false)
        )
    }

    init {
        addMovieDetailBasicsView()
    }

    private fun addMovieDetailBasicsView() {
        addView(binding.root)
    }

    fun setMovieDetailData(movieDetail: MovieDetail) {
        binding.tvTitle.text = movieDetail.originalTitle
        binding.tvReleaseDate.text = movieDetail.releaseDate
        binding.tvRuntime.text = movieDetail.runtime.let { time ->
            val hour = time / 60
            val minute = time % 60

            if (time > 60) {
                context.getString(R.string.label_runtime_hour_minute, hour, minute)
            } else if (time == 60) {
                context.getString(R.string.label_runtime_hour, hour)
            } else {
                context.getString(R.string.label_runtime_minute, minute)
            }
        }
        binding.tvOverview.text = movieDetail.overview

        binding.pbVoteAverage.let { view ->
            view.setTrackColor(
                (movieDetail.voteAverage * 10).toInt().let { percent ->
                    if (percent >= 70) {
                        ContextCompat.getColor(context, R.color.user_score_track)
                    } else {
                        ContextCompat.getColor(context, R.color.user_score_track_under_70)
                    }
                }
            )
            view.setIndicatorColor(
                (movieDetail.voteAverage * 10).toInt().let { percent ->
                    if (percent >= 70) {
                        ContextCompat.getColor(context, R.color.user_score_indicator)
                    } else {
                        ContextCompat.getColor(context, R.color.user_score_indicator_under_70)
                    }
                }
            )
            view.setProgress((movieDetail.voteAverage * 10).toInt())
            view.setLabelText(
                String.format(
                    context.getString(R.string.label_number_with_percent),
                    (movieDetail.voteAverage * 10).toInt()
                )
            )
            view.invalidate()
        }
    }
}
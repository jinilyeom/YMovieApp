package com.ymovie.app.ui.detail.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.ymovie.app.R
import com.ymovie.app.data.model.movie.Cast
import com.ymovie.app.data.model.movie.Crew
import com.ymovie.app.databinding.ViewMovieDetailCreditsBinding
import com.ymovie.app.ui.detail.adapter.MovieCastsAdapter
import com.ymovie.app.util.RecyclerViewItemDecoration
import com.ymovie.app.util.convertDpToPx
import com.ymovie.app.util.setItemOffset

class MovieDetailCreditsView(
    private val context: Context,
    private val attributeSet: AttributeSet
) : ConstraintLayout(context, attributeSet) {

    private val binding: ViewMovieDetailCreditsBinding by lazy {
        ViewMovieDetailCreditsBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.view_movie_detail_credits, this, false)
        )
    }

    private val movieCastsAdapter: MovieCastsAdapter by lazy {
        MovieCastsAdapter(ArrayList())
    }

    init {
        addMovieDetailCreditsView()
        initView()
    }

    private fun addMovieDetailCreditsView() {
        addView(binding.root)
    }

    private fun initView() {
        binding.rvCredits.let {
            it.layoutManager = LinearLayoutManager(context).apply {
                this.orientation = LinearLayoutManager.HORIZONTAL
            }
            it.adapter = movieCastsAdapter
            it.addItemDecoration(
                RecyclerViewItemDecoration(setItemOffset(leftPx = convertDpToPx(16F, resources)))
            )
        }
    }

    fun setHeaderText(headerText: String) {
        binding.tvHeader.text = headerText
    }

    fun setCasts(list: List<Cast>) {
        movieCastsAdapter.setItemToList(list)
    }

    fun setCrews(list: List<Crew>) {
        binding.tvDirector.text = list.filter {
            it.department == "Directing" && it.job == "Director"
        }.map {
            it.name
        }.let {
            val str = it.toString()
            str.substring(1, str.length - 1)
        }

        binding.tvWriter.text = list.filter {
            it.department == "Writing"
        }.map {
            it.name
        }.let {
            val str = it.toString()
            str.substring(1, str.length - 1)
        }
    }
}
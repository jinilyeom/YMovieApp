package com.ymovie.app.ui.home

import android.content.Context
import android.graphics.Rect
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ymovie.app.data.model.movie.Movie
import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.databinding.ItemHomeTypeDefaultBinding
import com.ymovie.app.util.RecyclerViewItemOffset
import com.ymovie.app.util.convertDpToPx

class HomeViewHolder(
    private val binding: ItemHomeTypeDefaultBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(context: Context, list: MovieList) {
        binding.tvHeader.text = list.header

        val rightPx = convertDpToPx(16F, context.resources)
        val rect = Rect(0, 0, rightPx, 0)

        val linearLayoutManager = LinearLayoutManager(context).apply {
            this.orientation = LinearLayoutManager.HORIZONTAL
        }

        binding.rvMovies.apply {
            this.layoutManager = linearLayoutManager
            this.adapter = MovieListAdapter(context, list.movies as MutableList<Movie>)
            this.addItemDecoration(RecyclerViewItemOffset(rect))
        }
    }
}
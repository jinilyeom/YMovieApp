package com.ymovie.app.ui.home.viewholder

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ymovie.app.data.model.movie.Movie
import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.databinding.ItemHomeTypeListBinding
import com.ymovie.app.ui.home.adapter.HomeMovieListAdapter
import com.ymovie.app.util.RecyclerViewItemDecoration
import com.ymovie.app.util.convertDpToPx
import com.ymovie.app.util.setItemOffset

class HomeListViewHolder(
    private val binding: ItemHomeTypeListBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(context: Context, list: MovieList) {
        binding.tvHeader.text = list.header

        val linearLayoutManager = LinearLayoutManager(context).apply {
            this.orientation = LinearLayoutManager.HORIZONTAL
        }

        binding.rvMovies.apply {
            this.layoutManager = linearLayoutManager
            this.adapter = HomeMovieListAdapter(context, list.movies as MutableList<Movie>)
            this.addItemDecoration(
                RecyclerViewItemDecoration(setItemOffset(leftPx = convertDpToPx(16F, context.resources)))
            )
        }
    }
}
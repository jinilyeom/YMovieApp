package com.ymovie.app.ui.home.viewholder

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.databinding.ItemHomeTypeListBinding
import com.ymovie.app.ui.home.HomeViewHolder
import com.ymovie.app.ui.home.adapter.HomeMovieListAdapter
import com.ymovie.app.util.RecyclerViewItemDecoration
import com.ymovie.app.util.convertDpToPx
import com.ymovie.app.util.setItemOffset

class HomeListViewHolder(
    private val binding: ItemHomeTypeListBinding,
    private val adapter: HomeMovieListAdapter
) : HomeViewHolder(binding) {

    override fun onInitView(context: Context) {
        val linearLayoutManager = LinearLayoutManager(context).apply {
            this.orientation = LinearLayoutManager.HORIZONTAL
        }

        binding.rvMovies.let {
            it.layoutManager = linearLayoutManager
            it.adapter = this.adapter
            it.addItemDecoration(
                RecyclerViewItemDecoration(setItemOffset(leftPx = convertDpToPx(16F, context.resources)))
            )
        }
    }

    override fun onBindView(list: MovieList) {
        binding.tvHeader.text = list.header

        adapter.setItemToList(list.movies ?: emptyList())
    }
}
package com.ymovie.app.ui.home.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.databinding.ItemHomeTypeListBinding
import com.ymovie.app.ui.home.adapter.HomeMovieListAdapter

class HomeListViewHolder(
    private val binding: ItemHomeTypeListBinding,
    private val adapter: HomeMovieListAdapter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(context: Context, list: MovieList) {
        binding.tvHeader.text = list.header

        adapter.setItemToList(
            list.movies ?: emptyList()
        )
    }
}
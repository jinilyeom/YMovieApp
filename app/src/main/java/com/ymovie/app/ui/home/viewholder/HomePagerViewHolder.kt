package com.ymovie.app.ui.home.viewholder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.databinding.ItemHomeTypePagerBinding
import com.ymovie.app.ui.home.adapter.HomeMoviePagerAdapter

class HomePagerViewHolder(
    private val binding: ItemHomeTypePagerBinding,
    private val adapter: HomeMoviePagerAdapter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(context: Context, list: MovieList) {
        adapter.setItemToList(
            list.movies ?: emptyList()
        )
    }
}
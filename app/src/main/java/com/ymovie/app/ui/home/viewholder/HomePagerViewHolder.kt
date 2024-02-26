package com.ymovie.app.ui.home.viewholder

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.databinding.ItemHomeTypePagerBinding
import com.ymovie.app.ui.home.HomeViewHolder
import com.ymovie.app.ui.home.adapter.HomeMoviePagerAdapter

class HomePagerViewHolder(
    private val binding: ItemHomeTypePagerBinding,
    private val adapter: HomeMoviePagerAdapter
) : HomeViewHolder(binding) {

    override fun onInitView(context: Context) {
        val linearLayoutManager = LinearLayoutManager(context).apply {
            this.orientation = LinearLayoutManager.HORIZONTAL
        }

        binding.viewPager.let {
            it.layoutManager = linearLayoutManager
            it.adapter = this.adapter
        }

        PagerSnapHelper().attachToRecyclerView(binding.viewPager)
    }

    override fun onBindView(list: MovieList) {
        adapter.setItemToList(list.movies ?: emptyList())
    }
}
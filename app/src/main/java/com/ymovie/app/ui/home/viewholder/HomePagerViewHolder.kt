package com.ymovie.app.ui.home.viewholder

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.ymovie.app.data.model.movie.Movie
import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.databinding.ItemHomeType1Binding
import com.ymovie.app.ui.home.adapter.HomeMoviePagerAdapter

class HomePagerViewHolder(
    private val binding: ItemHomeType1Binding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(context: Context, list: MovieList) {
        val linearLayoutManager = LinearLayoutManager(context).apply {
            this.orientation = LinearLayoutManager.HORIZONTAL
        }

        binding.viewPager.apply {
            this.layoutManager = linearLayoutManager
            this.adapter = HomeMoviePagerAdapter(context, list.movies as MutableList<Movie>)
        }

        PagerSnapHelper().apply {
            this.attachToRecyclerView(binding.viewPager)
        }
    }
}
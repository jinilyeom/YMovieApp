package com.ymovie.app.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.databinding.ItemHomeTypeListBinding
import com.ymovie.app.databinding.ItemHomeTypePagerBinding
import com.ymovie.app.ui.home.HomeViewType
import com.ymovie.app.ui.home.viewholder.HomePagerViewHolder
import com.ymovie.app.ui.home.viewholder.HomeListViewHolder
import com.ymovie.app.util.RecyclerViewItemDecoration
import com.ymovie.app.util.convertDpToPx
import com.ymovie.app.util.setItemOffset

class HomeAdapter(
    private val context: Context,
    private var homeData: MutableList<MovieList>
) : RecyclerView.Adapter<ViewHolder>() {
    private lateinit var homeMoviePagerAdapter: HomeMoviePagerAdapter
    private lateinit var homeMovieListAdapter: HomeMovieListAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            HomeViewType.MOVIE_PAGER_HORIZONTAL.ordinal -> {
                val binding = ItemHomeTypePagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

                initPagerLayout(binding)
                HomePagerViewHolder(binding, homeMoviePagerAdapter)
            }

            else -> {
                val binding = ItemHomeTypeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

                initListLayout(binding)
                HomeListViewHolder(binding, homeMovieListAdapter)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (homeData[position].viewType) {
            HomeViewType.MOVIE_PAGER_HORIZONTAL.ordinal -> {
                (holder as HomePagerViewHolder).bind(context, homeData[position])
            }

            else -> {
                (holder as HomeListViewHolder).bind(context, homeData[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return homeData.size
    }

    override fun getItemViewType(position: Int): Int {
        return homeData[position].viewType
    }

    private fun initPagerLayout(binding: ItemHomeTypePagerBinding) {
        val linearLayoutManager = LinearLayoutManager(context).apply {
            this.orientation = LinearLayoutManager.HORIZONTAL
        }

        homeMoviePagerAdapter = HomeMoviePagerAdapter(context, ArrayList())

        binding.viewPager.let {
            it.layoutManager = linearLayoutManager
            it.adapter = homeMoviePagerAdapter
        }

        PagerSnapHelper().attachToRecyclerView(binding.viewPager)
    }

    private fun initListLayout(binding: ItemHomeTypeListBinding) {
        val linearLayoutManager = LinearLayoutManager(context).apply {
            this.orientation = LinearLayoutManager.HORIZONTAL
        }

        homeMovieListAdapter = HomeMovieListAdapter(context, ArrayList())

        binding.rvMovies.let {
            it.layoutManager = linearLayoutManager
            it.adapter = homeMovieListAdapter
            it.addItemDecoration(
                RecyclerViewItemDecoration(setItemOffset(leftPx = convertDpToPx(16F, context.resources)))
            )
        }
    }

    fun addItemToList(list: MovieList) {
        val position = if (homeData.isEmpty()) 0 else homeData.size

        homeData.add(list)

        notifyItemInserted(position)
    }
}
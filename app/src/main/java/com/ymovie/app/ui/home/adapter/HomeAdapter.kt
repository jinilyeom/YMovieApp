package com.ymovie.app.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.databinding.ItemHomeType1Binding
import com.ymovie.app.databinding.ItemHomeTypeDefaultBinding
import com.ymovie.app.ui.home.HomeViewType
import com.ymovie.app.ui.home.viewholder.HomePagerViewHolder
import com.ymovie.app.ui.home.viewholder.HomeViewHolder

class HomeAdapter(
    private val context: Context,
    private var homeData: MutableList<MovieList>
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            HomeViewType.MOVIE_PAGER_HORIZONTAL.ordinal -> {
                HomePagerViewHolder(
                    ItemHomeType1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }

            else -> {
                HomeViewHolder(
                    ItemHomeTypeDefaultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (homeData[position].viewType) {
            HomeViewType.MOVIE_PAGER_HORIZONTAL.ordinal -> {
                (holder as HomePagerViewHolder).bind(context, homeData[position])
            }

            else -> {
                (holder as HomeViewHolder).bind(context, homeData[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return homeData.size
    }

    override fun getItemViewType(position: Int): Int {
        return homeData[position].viewType
    }

    fun addItemToList(list: MovieList) {
        val position = if (homeData.isEmpty()) 0 else homeData.size

        homeData.add(list)

        notifyItemInserted(position)
    }
}
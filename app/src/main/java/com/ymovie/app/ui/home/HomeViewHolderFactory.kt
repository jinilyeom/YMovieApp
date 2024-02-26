package com.ymovie.app.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ymovie.app.databinding.ItemHomeTypeListBinding
import com.ymovie.app.databinding.ItemHomeTypePagerBinding
import com.ymovie.app.ui.home.adapter.HomeMovieListAdapter
import com.ymovie.app.ui.home.adapter.HomeMoviePagerAdapter
import com.ymovie.app.ui.home.viewholder.HomeListViewHolder
import com.ymovie.app.ui.home.viewholder.HomePagerViewHolder

object HomeViewHolderFactory {
    fun create(context: Context, parent: ViewGroup, viewType: Int): HomeViewHolder {
        return when (viewType) {
            HomeViewType.MOVIE_PAGER_HORIZONTAL.ordinal -> {
                val binding = ItemHomeTypePagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

                HomePagerViewHolder(binding, HomeMoviePagerAdapter(context, ArrayList()))
            }

            else -> {
                val binding = ItemHomeTypeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

                HomeListViewHolder(binding, HomeMovieListAdapter(context, ArrayList()))
            }
        }
    }

    fun bind(holder: RecyclerView.ViewHolder, viewType: Int): HomeViewHolder {
        return when (viewType) {
            HomeViewType.MOVIE_PAGER_HORIZONTAL.ordinal -> {
                (holder as HomePagerViewHolder)
            }

            else -> {
                (holder as HomeListViewHolder)
            }
        }
    }
}
package com.ymovie.app.ui.home.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.ui.home.HomeViewHolderFactory

class HomeAdapter(
    private val context: Context,
    private var homeData: MutableList<MovieList>
) : RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val newHolder = HomeViewHolderFactory.create(context, parent, viewType)
        newHolder.onInitView(context)

        return newHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newHolder = HomeViewHolderFactory.bind(holder, homeData[position].viewType)
        newHolder.onBindView(homeData[position])
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
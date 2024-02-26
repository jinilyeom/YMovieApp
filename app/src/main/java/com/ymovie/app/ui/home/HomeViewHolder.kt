package com.ymovie.app.ui.home

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.ymovie.app.data.model.movie.MovieList

abstract class HomeViewHolder(
    binding: ViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    abstract fun onInitView(context: Context)
    abstract fun onBindView(list: MovieList)
}
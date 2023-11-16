package com.ymovie.app.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ymovie.app.data.model.movie.Movie
import com.ymovie.app.databinding.ItemHomeMovieListBinding
import com.ymovie.app.ui.home.viewholder.HomeMovieListViewHolder

class HomeMovieListAdapter(
    private val context: Context,
    private var movies: MutableList<Movie>
) : RecyclerView.Adapter<HomeMovieListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMovieListViewHolder {
        val binding = ItemHomeMovieListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HomeMovieListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeMovieListViewHolder, position: Int) {
        holder.bind(context, movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}
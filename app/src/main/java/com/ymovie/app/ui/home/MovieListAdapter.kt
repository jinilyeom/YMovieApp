package com.ymovie.app.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ymovie.app.data.model.movie.Movie
import com.ymovie.app.databinding.ItemMovieTypeDefaultBinding

class MovieListAdapter(
    private val context: Context,
    private var movies: MutableList<Movie>
) : RecyclerView.Adapter<MovieListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        val binding = ItemMovieTypeDefaultBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MovieListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        holder.bind(context, movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}
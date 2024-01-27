package com.ymovie.app.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ymovie.app.data.model.movie.Movie
import com.ymovie.app.databinding.ItemHomeMoviePagerBinding
import com.ymovie.app.ui.home.viewholder.HomeMoviePagerViewHolder

class HomeMoviePagerAdapter(
    private val context: Context,
    private var movies: MutableList<Movie>
) : RecyclerView.Adapter<HomeMoviePagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeMoviePagerViewHolder {
        val binding = ItemHomeMoviePagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return HomeMoviePagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeMoviePagerViewHolder, position: Int) {
        holder.bind(context, movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setItemToList(list: List<Movie>) {
        movies.let {
            if (it.isNotEmpty()) it.clear()

            it.addAll(list)
        }

        notifyDataSetChanged()
    }
}
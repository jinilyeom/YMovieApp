package com.ymovie.app.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ymovie.app.data.model.movie.Movie
import com.ymovie.app.databinding.ItemSearchBinding
import com.ymovie.app.ui.search.viewholder.SearchViewHolder

class SearchAdapter(
    private val context: Context,
    private var movies: MutableList<Movie>
) : RecyclerView.Adapter<SearchViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val binding = ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(context, movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun addItemToList(list: List<Movie>) {
        movies.let {
            it.addAll(list)

            notifyDataSetChanged()
        }
    }

    fun clearList() {
        if (movies.isEmpty()) {
            return
        }

        movies.clear()
    }
}
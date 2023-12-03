package com.ymovie.app.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ymovie.app.data.model.movie.Cast
import com.ymovie.app.databinding.ItemMovieCastsBinding
import com.ymovie.app.ui.detail.viewholder.MovieCastsViewHolder

class MovieCastsAdapter(private val casts: MutableList<Cast>) : RecyclerView.Adapter<ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return MovieCastsViewHolder(
            ItemMovieCastsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as MovieCastsViewHolder).bind(casts[position])
    }

    override fun getItemCount(): Int {
        return casts.size
    }

    fun setItemToList(list: List<Cast>) {
        casts.let {
            if (it.isNotEmpty()) {
                it.clear()
            }

            it.addAll(
                list.filter { cast -> cast.order <= 10 }
            )

            notifyItemInserted(0)
        }
    }
}
package com.ymovie.app.ui.detail.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ymovie.app.data.model.movie.Cast
import com.ymovie.app.databinding.ItemMovieCastsBinding
import com.ymovie.app.network.NetworkConstants

class MovieCastsViewHolder(
    private val binding: ItemMovieCastsBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(cast: Cast) {
        Glide.with(binding.imvProfile)
            .load(NetworkConstants.IMAGE_BASE_URL_W200 + cast.profilePath)
            .centerCrop()
            .into(binding.imvProfile)

        binding.tvName.text = cast.name
        binding.tvCharacter.text = cast.character
    }
}
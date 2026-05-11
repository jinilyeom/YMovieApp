package com.moviery.android.data.model.movie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String
)
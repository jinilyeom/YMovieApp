package com.ymovie.app.data.model.movie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BelongsToCollection(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("poster_path")
    @Expose
    var posterPath: String,
    @SerializedName("backdrop_path")
    @Expose
    var backdropPath: String
)
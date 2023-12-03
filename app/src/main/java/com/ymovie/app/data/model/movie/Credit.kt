package com.ymovie.app.data.model.movie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Credit(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("cast")
    @Expose
    var casts: List<Cast>,
    @SerializedName("crew")
    @Expose
    var crews: List<Crew>
)

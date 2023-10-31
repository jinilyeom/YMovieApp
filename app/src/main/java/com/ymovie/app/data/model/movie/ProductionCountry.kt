package com.ymovie.app.data.model.movie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductionCountry(
    @SerializedName("iso_3166_1")
    @Expose
    var iso31661: String,
    @SerializedName("name")
    @Expose
    var name: String
)
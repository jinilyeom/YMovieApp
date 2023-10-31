package com.ymovie.app.data.model.movie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductionCompany(
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("logo_path")
    @Expose
    var logoPath: String,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("origin_country")
    @Expose
    var originCountry: String
)
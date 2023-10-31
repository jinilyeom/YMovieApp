package com.ymovie.app.data.model.movie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SpokenLanguage(
    @SerializedName("english_name")
    @Expose
    var englishName: String,
    @SerializedName("iso_639_1")
    @Expose
    var iso6391: String,
    @SerializedName("name")
    @Expose
    var name: String
)
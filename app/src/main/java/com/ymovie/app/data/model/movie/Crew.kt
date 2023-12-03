package com.ymovie.app.data.model.movie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Crew(
    @SerializedName("adult")
    @Expose
    var adult: Boolean,
    @SerializedName("gender")
    @Expose
    var gender: Int,
    @SerializedName("id")
    @Expose
    var id: Int,
    @SerializedName("known_for_department")
    @Expose
    var knownForDepartment: String,
    @SerializedName("name")
    @Expose
    var name: String,
    @SerializedName("original_name")
    @Expose
    var originalName: String,
    @SerializedName("popularity")
    @Expose
    var popularity: Float,
    @SerializedName("profile_path")
    @Expose
    var profilePath: String,
    @SerializedName("credit_id")
    @Expose
    var creditId: String,
    @SerializedName("department")
    @Expose
    var department: String,
    @SerializedName("job")
    @Expose
    var job: String
)
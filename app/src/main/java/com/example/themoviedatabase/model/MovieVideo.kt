package com.example.themoviedatabase.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MovieVideo(
    @SerializedName("id")
    var videoId: String,
    @SerializedName("iso_639_1")
    val iso_639_1: String,
    @SerializedName("iso_3166_1")
    val iso_3166_1: String,
    @SerializedName("key")
    val videoKey: String,
    @SerializedName("name")
    val videoName: String,
    @SerializedName("site")
    val videoSite: String,
    @SerializedName("size")
    val videoSize: Int,
    @SerializedName("type")
    val videoType: String
) : Serializable
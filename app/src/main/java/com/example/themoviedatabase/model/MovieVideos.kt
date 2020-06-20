package com.example.themoviedatabase.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MovieVideos(
    @SerializedName("id")
    var movieId: Int = 0,
    @SerializedName("results")
    val movieVideos: ArrayList<MovieVideo> = ArrayList()
): Serializable
package com.example.themoviedatabase.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Genre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    var moviePages: ArrayList<MoviePage>? = ArrayList()
) : Serializable
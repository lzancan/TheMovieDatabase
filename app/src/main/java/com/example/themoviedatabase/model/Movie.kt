package com.example.themoviedatabase.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("movie_id")
    val movieId: String?,

    @SerializedName("movie_name")
    val movieName: String?,

    @SerializedName("movie_description")
    val movieDescription: String?,

    @SerializedName("movie_gender")
    val movieGender: String?
)
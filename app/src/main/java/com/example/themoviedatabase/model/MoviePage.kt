package com.example.themoviedatabase.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MoviePage(
    @SerializedName("page")
    val page: Int?,
    @SerializedName("total_results")
    val totalResults: Int?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("results")
    val results: List<Movie>?
) : Serializable
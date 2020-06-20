package com.example.themoviedatabase.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(primaryKeys = ["genre_id", "movies_page"])
class MoviePage(
    @ColumnInfo(name = "genre_id")
    var genreId: Int,
    @ColumnInfo(name = "movies_page")
    @SerializedName("page")
    val page: Int,
    @ColumnInfo(name = "movies_total_results")
    @SerializedName("total_results")
    val totalResults: Int?,
    @ColumnInfo(name = "movies_total_pages")
    @SerializedName("total_pages")
    val totalPages: Int?,
    @ColumnInfo(name = "movies_results")
    @SerializedName("results")
    val results: ArrayList<Movie>?
) : Serializable
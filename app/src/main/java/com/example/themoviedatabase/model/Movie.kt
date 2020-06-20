package com.example.themoviedatabase.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class Movie(
    @SerializedName("id")
    val movieId: Int = 0,
    @SerializedName("title")
    val movieTitle: String?,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("overview")
    val movieOverview: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("adult")
    val isAdult: Boolean?,
    @SerializedName("video")
    val hasVideo: Boolean?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("vote_count")
    val voteCount: Long?,
    @SerializedName("vote_average")
    val voteAverage: Float?,
    @SerializedName("popularity")
    val popularity: Float?,
    @SerializedName("genre_ids")
    val genreIds: ArrayList<Int>?,
    @SerializedName("movie_gender")
    val movieGender: String?
) : Serializable
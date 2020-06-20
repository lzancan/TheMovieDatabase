package com.example.themoviedatabase.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*
import kotlin.collections.ArrayList

class RoomTypeConverters {

    private val gson = Gson()

    @TypeConverter
    fun moviePagesListToString(moviePagesList: List<MoviePage>?): String? {
        return if (moviePagesList == null) null else gson.toJson(moviePagesList)
    }

    @TypeConverter
    fun stringToMoviesList(data: String?): ArrayList<Movie>? {
        if (data == null || data == "null") {
            return ArrayList()
        }

        val listType = object : TypeToken<ArrayList<Movie>>() {
        }.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun moviesListToString(moviesList: ArrayList<Movie>?): String? {
        return if (moviesList == null) null else gson.toJson(moviesList)
    }
}
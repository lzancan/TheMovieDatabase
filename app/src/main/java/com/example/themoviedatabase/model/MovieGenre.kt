package com.example.themoviedatabase.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MovieGenre (
    @SerializedName("genres")
    val genres: List<Genre>?
): Serializable
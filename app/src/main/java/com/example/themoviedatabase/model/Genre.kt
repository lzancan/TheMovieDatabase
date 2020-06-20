package com.example.themoviedatabase.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity
class Genre(
    @PrimaryKey
    @ColumnInfo(name = "genre_id")
    @SerializedName("id")
    val id: Int,
    @ColumnInfo(name = "genre_name")
    @SerializedName("name")
    val name: String
) : Serializable
package com.example.themoviedatabase.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MoviesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg genre: Genre): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoviePage(moviePage: MoviePage): Long

    @Query("SELECT * FROM genre")
    suspend fun getAllGenres(): List<Genre>

    @Query("SELECT * FROM moviepage where genre_id =:genreId AND movies_page =:page")
    suspend fun getMoviesPageFromGenre(genreId: Int, page: Int): MoviePage?

    @Query("DELETE FROM genre")
    suspend fun deleteAllGenres()

    @Query("DELETE FROM moviepage")
    suspend fun deleteAllMoviePages()
}
package com.example.themoviedatabase.model

import com.example.themoviedatabase.model.MoviesApiService.Companion.API_KEY
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("discover/movie?api_key=${API_KEY}")
    fun getMoviesPageFromGenre(@Query("with_genres") genreId: String, @Query("page") page: String): Single<MoviePage>

    @GET("genre/movie/list?api_key=${API_KEY}&language=en-US")
    fun getGenres(): Single<MovieGenre>

    @GET("movie/{movieId}?api_key=${API_KEY}&language=en-US")
    fun getMovieDetails(@Path("movieId") movieId: String): Single<Movie>

}
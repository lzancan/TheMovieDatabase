package com.example.themoviedatabase.model

import com.example.themoviedatabase.model.MoviesApiService.Companion.API_KEY
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface MoviesApi {
    @GET("/${API_KEY}")
    fun getMovies(): Single<List<Movie>>

    @GET("genre/movie/list?api_key=${API_KEY}")
    fun getGenreMovies(): Single<List<Movie>>

    @GET("movie/{movieId}?api_key=${API_KEY}&language=en-US")
    fun getMovieDetails(@Path("movieId") movieId: String): Single<Movie>

}
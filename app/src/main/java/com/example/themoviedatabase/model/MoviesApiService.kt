package com.example.themoviedatabase.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MoviesApiService {

    companion object {

        const val API_KEY = "2ab170fdb4f3def22f9f0b84408dd2ba"
        const val BASE_URL = "https://api.themoviedb.org/3"
    }

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MoviesApi::class.java)

    fun getMovies(): Single<List<Movie>> {
        return api.getMovies()
    }

    fun getGenreMovies(): Single<List<Movie>> {
        return api.getGenreMovies()
    }

    fun getMovieDetails(movieId: String): Single<Movie> {
        return api.getMovieDetails(movieId)
    }
}
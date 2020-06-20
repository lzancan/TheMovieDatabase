package com.example.themoviedatabase.model

import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MoviesApiService {

    companion object {

        const val API_KEY = "2ab170fdb4f3def22f9f0b84408dd2ba"
        const val BASE_URL = "https://api.themoviedb.org/3/"
    }

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(MoviesApi::class.java)

    fun getMoviesFromGenre(genreId: String, page: String): Single<MoviePage> {
        return api.getMoviesPageFromGenre(genreId, page)
    }

    fun getGenres(): Single<MovieGenre> {
        return api.getGenres()
    }

    fun getSimilarMoviesFromMovie(movieId: String, page: String): Single<MoviePage> {
        return api.getSimilarMoviesPageFromMovie(movieId, page)
    }

    fun getVideosFromMovie(movieId: String): Single<MovieVideos> {
        return api.getVideosFromMovie(movieId)
    }
}
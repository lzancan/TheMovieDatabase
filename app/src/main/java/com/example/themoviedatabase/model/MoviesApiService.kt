package com.example.themoviedatabase.model

import com.example.themoviedatabase.di.DaggerApiComponent
import io.reactivex.Single
import javax.inject.Inject

class MoviesApiService {

    companion object {

        const val API_KEY = "2ab170fdb4f3def22f9f0b84408dd2ba"
    }

    @Inject
    lateinit var api: MoviesApi

    init {
        DaggerApiComponent.create().inject(this)
    }

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
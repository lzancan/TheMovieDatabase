package com.example.themoviedatabase.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themoviedatabase.model.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MoviesViewModel: ViewModel() {

    val genres by lazy { MutableLiveData<List<Genre>>() }
    val movies by lazy { MutableLiveData<List<Movie>>() }

    private val moviesApiService = MoviesApiService()
    private val disposable = CompositeDisposable()

    fun getGenres() {
        disposable.add(
            moviesApiService.getGenres()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieGenre>() {

                    override fun onSuccess(movieGenre: MovieGenre) {
                        genres.value = movieGenre.genres
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        Log.d("genre", "error: $e")
                    }

                })
        )
    }

    fun getMoviesFromGenre(genreId: Int, page: Int) {
        disposable.add(
            moviesApiService.getMoviesFromGenre(genreId.toString(), page.toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MoviePage>() {

                    override fun onSuccess(moviePage: MoviePage) {
                        movies.value = moviePage.results
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        //do something
                    }

                })
        )
    }
}
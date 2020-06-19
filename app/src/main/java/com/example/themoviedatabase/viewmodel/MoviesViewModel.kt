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

    val genreMoviesReceived = MutableLiveData<Int>()

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
                        movieGenre.genres?.forEach {
                            getMoviesFromGenre(it.id, 1)
                        }
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
                        if(genres.value?.firstOrNull { it.id == genreId }?.moviePages == null){
                            genres.value?.firstOrNull { it.id == genreId }?.moviePages = ArrayList()
                        }
                        val moviePages = genres.value?.firstOrNull { it.id == genreId }?.moviePages
                        if(moviePages?.none{it.page == moviePage.page} == true) {
                            moviePages.add((moviePage.page?:1) - 1, moviePage)
                        }
                        genreMoviesReceived.value = genreId
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        //do something
                    }

                })
        )
    }
}
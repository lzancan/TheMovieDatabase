package com.example.themoviedatabase.viewmodel

import androidx.lifecycle.ViewModel
import com.example.themoviedatabase.model.Movie
import com.example.themoviedatabase.model.MoviesApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

class MoviesViewModel: ViewModel() {

    private val moviesApiService = MoviesApiService()
    private val disposable = CompositeDisposable()

    fun getMoviesFromRemote() {
        disposable.add(
            moviesApiService.getMovies()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Movie>>() {

                    override fun onSuccess(moviesList: List<Movie>) {
                        //do something
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        //do something
                    }

                })
        )
    }
}
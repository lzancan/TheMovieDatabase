package com.example.themoviedatabase.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.themoviedatabase.model.*
import com.example.themoviedatabase.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException

class MoviesViewModel(application: Application) : BaseViewModel(application) {

    private var prefHelper = SharedPreferencesHelper(application)
    private var refreshTime = 5 * 60 * 1000 * 1000 * 100L

    val genres by lazy { MutableLiveData<List<Genre>>() }
    val moviePages by lazy { MutableLiveData<HashMap<Int, ArrayList<MoviePage>>>() }

    val genreMoviesReceived = MutableLiveData<MoviePage>()

    val downLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    private val moviesApiService = MoviesApiService()
    private val disposable = CompositeDisposable()

    fun refresh() {
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getGenresFromDatabase()
        } else {
            getGenresFromApi()
        }
    }

    fun refreshBypassCache(){
        getGenresFromApi()
    }

    private fun genresRetrieved(genresList: List<Genre>) {
        genres.value = genresList
        downLoadError.value = false
        loading.value = false
        genresList.forEach {
            getMoviesFromGenre(it.id, 1)
        }
    }

    private fun moviesPageRetrieved(moviePage: MoviePage) {
        if (moviePages.value == null) {
            moviePages.value = hashMapOf()
        }
        if (moviePages.value!![moviePage.genreId] == null) {
            moviePages.value!![moviePage.genreId] = ArrayList()
        }
        moviePages.value!![moviePage.genreId]?.let { moviePagesList ->
            if (moviePagesList.none { it.page == moviePage.page }) {
                moviePagesList.add(moviePage)
            }
        }
        genreMoviesReceived.value = moviePage
    }

    private fun getGenresFromDatabase() {
        loading.value = true
        launch {
            val genres = MoviesDatabase(getApplication()).moviesDao().getAllGenres()
            Log.d("exc19062020", "genresretrievedfromdatabase")
            genresRetrieved(genres)
        }
    }

    private fun getGenresFromApi() {
        disposable.add(
            moviesApiService.getGenres()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieGenre>() {

                    override fun onSuccess(movieGenre: MovieGenre) {
                        Log.d("exc19062020", "genresretrievedfromApi")
                        movieGenre.genres?.let { genresList ->
                            storeGenresInDatabase(genresList)
                            genresList.forEach { genre ->
                                getMoviesFromGenreFromApi(genre.id, 1)
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        downLoadError.value = true
                        loading.value = false
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun getMoviesFromGenre(genreId: Int, page: Int) {
        launch {
            val moviesPage = MoviesDatabase(getApplication()).moviesDao().getMoviesPageFromGenre(genreId, page)
            moviesPage?.let {
                Log.d("exc19062020", "movies from genre $genreId retrieved from database")
                moviesPageRetrieved(moviesPage)
            } ?: getMoviesFromGenreFromApi(genreId, page)
        }
    }

    fun getMoviesFromGenreFromApi(genreId: Int, page: Int) {
        disposable.add(
            moviesApiService.getMoviesFromGenre(genreId.toString(), page.toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MoviePage>() {

                    override fun onSuccess(moviePage: MoviePage) {
                        Log.d("exc19062020", "movies from genre $genreId retrieved from Api")
                        moviePage.genreId = genreId
                        storeMoviePageInDatabase(moviePage)
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun storeGenresInDatabase(list: List<Genre>) {
        launch {
            val dao = MoviesDatabase(getApplication()).moviesDao()
            dao.deleteAllGenres()
            dao.deleteAllMoviePages()
            dao.insertAll(*list.toTypedArray())
            genresRetrieved(list)
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    private fun storeMoviePageInDatabase(moviePage: MoviePage) {
        launch {
            val dao = MoviesDatabase(getApplication()).moviesDao()
            dao.insertMoviePage(moviePage)
            moviesPageRetrieved(moviePage)
        }
    }

    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()
        try {
            val cachePreferenceInt = cachePreference?.toInt() ?: 5 * 60
            refreshTime = cachePreferenceInt.times(1000 * 1000 * 1000L)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
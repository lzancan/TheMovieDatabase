package com.example.themoviedatabase.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.themoviedatabase.di.*
import com.example.themoviedatabase.di.ContextModule.CONTEXT_APP
import com.example.themoviedatabase.model.*
import com.example.themoviedatabase.util.SharedPreferencesHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import javax.inject.Inject

class MoviesViewModel(application: Application) : BaseViewModel(application) {

    @Inject
    lateinit var moviesApiService: MoviesApiService

    @Inject
    @field:ContextModule.TypeOfContext(CONTEXT_APP)
    lateinit var moviesDatabase: MoviesDatabase

    @Inject
    @field:ContextModule.TypeOfContext(CONTEXT_APP)
    lateinit var prefHelper: SharedPreferencesHelper

    private var refreshTime = 5 * 60 * 1000 * 1000 * 100L

    val toolbarName = MutableLiveData<String>()

    val genres by lazy { MutableLiveData<List<Genre>>() }
    val moviePages by lazy { MutableLiveData<HashMap<Int, ArrayList<MoviePage>>>() }
    val similarMovies by lazy { MutableLiveData<List<Movie>>() }
    val videosFromMovie by lazy { MutableLiveData<List<MovieVideo>>() }

    var isRequestingMovies = false

    val genreMoviesReceived = MutableLiveData<MoviePage>()

    val downLoadError = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()

    private val disposable = CompositeDisposable()

    private var injected = false

    fun inject() {
        if (!injected) {
            DaggerViewModelComponent.builder()
                .appModule(AppModule(getApplication()))
                .build()
                .inject(this)
        }
    }

    fun refreshGenres() {
        inject()
        checkCacheDuration()
        val updateTime = prefHelper.getUpdateTime()
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            getGenresFromDatabase()
        } else {
            getGenresFromApi()
        }
    }

    fun refreshBypassCache() {
        inject()
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
            val genres = moviesDatabase.moviesDao().getAllGenres()
            genresRetrieved(genres)
        }
    }

    fun getGenresFromApi() {
        disposable.add(
            moviesApiService.getGenres()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieGenre>() {

                    override fun onSuccess(movieGenre: MovieGenre) {
                        movieGenre.genres?.let { genresList ->
                            genresRetrieved(genresList)
                            storeGenresInDatabase(genresList)
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

    fun getMoviesFromGenre(genreId: Int, page: Int) {
        launch {
            val moviesPage = moviesDatabase.moviesDao().getMoviesPageFromGenre(genreId, page)
            moviesPage?.let {
                moviesPageRetrieved(moviesPage)
            } ?: getMoviesFromGenreFromApi(genreId, page)
        }
    }

    fun getMoviesFromGenreFromApi(genreId: Int, page: Int, storeInDatabase: Boolean = true) {
        disposable.add(
            moviesApiService.getMoviesFromGenre(genreId.toString(), page.toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MoviePage>() {

                    override fun onSuccess(moviePage: MoviePage) {
                        moviePage.genreId = genreId
                        moviesPageRetrieved(moviePage)
                        if(storeInDatabase) {
                            storeMoviePageInDatabase(moviePage)
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
        )
    }

    private fun storeGenresInDatabase(list: List<Genre>) {
        launch {
            val dao = moviesDatabase.moviesDao()
            dao.deleteAllGenres()
            dao.deleteAllMoviePages()
            dao.insertAll(*list.toTypedArray())
        }
        prefHelper.saveUpdateTime(System.nanoTime())
    }

    fun getSimilarMovies(movieId: Int, page: Int = 1) {
        disposable.add(
            moviesApiService.getSimilarMoviesFromMovie(movieId.toString(), page.toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MoviePage>() {

                    override fun onSuccess(moviePage: MoviePage) {
                        similarMovies.value = moviePage.results
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        similarMovies.value = ArrayList()
                    }

                })
        )
    }

    fun getVideosFromMovie(movieId: Int) {
        disposable.add(
            moviesApiService.getVideosFromMovie(movieId.toString())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<MovieVideos>() {

                    override fun onSuccess(movieVideos: MovieVideos) {
                        videosFromMovie.value = movieVideos.movieVideos
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        videosFromMovie.value = ArrayList()
                    }

                })
        )
    }

    private fun storeMoviePageInDatabase(moviePage: MoviePage) {
        launch {
            val dao = moviesDatabase.moviesDao()
            dao.insertMoviePage(moviePage)
        }
    }

    private fun checkCacheDuration() {
        val cachePreference = prefHelper.getCacheDuration()
        try {
            val cachePreferenceInt = cachePreference?.toIntOrNull() ?: 5 * 60
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
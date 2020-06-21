package com.example.themoviedatabase

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import com.example.themoviedatabase.di.AppModule
import com.example.themoviedatabase.di.DaggerViewModelComponent
import com.example.themoviedatabase.model.*
import com.example.themoviedatabase.util.SharedPreferencesHelper
import com.example.themoviedatabase.viewmodel.MoviesViewModel
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Callable
import java.util.concurrent.Executor


class MoviesViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var moviesApiService: MoviesApiService

    @Mock
    lateinit var prefs: SharedPreferencesHelper

    @Mock
    lateinit var roomDatabase: MoviesDatabase

    private val application = Mockito.mock(Application::class.java)

    private var moviesViewModel = MoviesViewModel(application)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        DaggerViewModelComponent.builder()
            .appModule(AppModule(application))
            .apiModule(ApiModuleTest(moviesApiService))
            .prefsModule(PrefsModuleTest(prefs))
            .roomModule(RoomModuleTest(roomDatabase))
            .build()
            .inject(moviesViewModel)
    }

    @Before
    fun setupRxSchedulers() {
        val immediate = object: Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
            }
        }

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { h: Callable<Scheduler?>? -> Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler{ h: Scheduler? -> Schedulers.trampoline() }
        RxJavaPlugins.setInitIoSchedulerHandler { immediate }
        RxJavaPlugins.setInitComputationSchedulerHandler { immediate }
        RxJavaPlugins.setInitNewThreadSchedulerHandler { immediate }
        RxJavaPlugins.setInitSingleSchedulerHandler { immediate }
    }

    @Before
    fun setupCoroutines(){
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getGenresSuccess() {
        val movieGenre = MovieGenre(listOf(Genre(1, "Genre x")))

        val testSingle = Single.just(movieGenre)

        Mockito.`when`(moviesApiService.getGenres()).thenReturn(testSingle)

        moviesViewModel.getGenresFromApi()

        Assert.assertEquals(1, moviesViewModel.genres.value?.size)
        Assert.assertEquals(false, moviesViewModel.downLoadError.value)
        Assert.assertEquals(false, moviesViewModel.loading.value)
    }

    @Test
    fun getGenresFailure() {
        val testSingle = Single.error<MovieGenre>(Throwable())

        Mockito.`when`(moviesApiService.getGenres()).thenReturn(testSingle)

        moviesViewModel.getGenresFromApi()

        Assert.assertEquals(null, moviesViewModel.genres.value?.size)
        Assert.assertEquals(true, moviesViewModel.downLoadError.value)
        Assert.assertEquals(false, moviesViewModel.loading.value)
    }

    @Test
    fun getMoviesSuccess() {
        val moviePage = MoviePage(16, 1, 1000, 5, arrayListOf(
            Movie(1, "Filme 1", "Filme 1",
        "Movie overview 1", "2020-06-01", "en", "blabla.jpg", false,
                hasVideo = true,
                posterPath = "blabla.jpg",
                voteCount = 50,
                voteAverage = 9.5f,
                popularity = 1.5f,
                genreIds = arrayListOf(),
                movieGender = "Animation"
            ),
            Movie(1, "Filme 2", "Filme 2",
                "Movie overview 2", "2020-05-02", "en", "blabla.jpg", false,
                hasVideo = true,
                posterPath = "blabla.jpg",
                voteCount = 51,
                voteAverage = 9.6f,
                popularity = 5.5f,
                genreIds = arrayListOf(),
                movieGender = "Animation"
            )))

        val testSingle = Single.just(moviePage)
        Mockito.`when`(moviesApiService.getMoviesFromGenre(16.toString(), 1.toString())).thenReturn(testSingle)

        moviesViewModel.getMoviesFromGenreFromApi(16, 1)

        Assert.assertEquals(1, moviesViewModel.moviePages.value?.values?.size)
        Assert.assertEquals(2, moviesViewModel.moviePages.value?.get(16)?.firstOrNull()?.results?.size)
    }

    @Test
    fun getMoviesFailure() {
        val testSingle = Single.error<MoviePage>(Throwable())
        Mockito.`when`(moviesApiService.getMoviesFromGenre(16.toString(), 1.toString())).thenReturn(testSingle)
        moviesViewModel.getMoviesFromGenre(16, 1)
        Assert.assertEquals(null, moviesViewModel.moviePages.value?.values?.size)
        Assert.assertEquals(null, moviesViewModel.moviePages.value?.get(16)?.firstOrNull()?.results?.size)
    }

    @Test
    fun getSimilarMoviesFromMovieSuccess() {
        val moviePage = MoviePage(16, 1, 1000, 5, arrayListOf(
            Movie(1, "Filme 1", "Filme 1",
                "Movie overview 1", "2020-06-01", "en", "blabla.jpg", false,
                hasVideo = true,
                posterPath = "blabla.jpg",
                voteCount = 50,
                voteAverage = 9.5f,
                popularity = 1.5f,
                genreIds = arrayListOf(),
                movieGender = "Animation"
            ),
            Movie(1, "Filme 2", "Filme 2",
                "Movie overview 2", "2020-05-02", "en", "blabla.jpg", false,
                hasVideo = true,
                posterPath = "blabla.jpg",
                voteCount = 51,
                voteAverage = 9.6f,
                popularity = 5.5f,
                genreIds = arrayListOf(),
                movieGender = "Animation"
            )))

        val testSingle = Single.just(moviePage)
        Mockito.`when`(moviesApiService.getSimilarMoviesFromMovie(16.toString(), 1.toString())).thenReturn(testSingle)

        moviesViewModel.getMoviesFromGenre(16, 1)

        Assert.assertEquals(null, moviesViewModel.similarMovies.value?.size)
    }

    @Test
    fun getSimilarMoviesFromMovieFailure() {
        val testSingle = Single.error<MoviePage>(Throwable())
        Mockito.`when`(moviesApiService.getSimilarMoviesFromMovie(16.toString(), 1.toString())).thenReturn(testSingle)
        moviesViewModel.getMoviesFromGenre(16, 1)
        Assert.assertEquals(null, moviesViewModel.similarMovies.value?.size)
    }

    @Test
    fun getVideosFromMovieSuccess() {
        val movieVideos = MovieVideos(16, arrayListOf(
            MovieVideo("123", "iso1_1", "iso1_2",
                "123456789", "Filme 1 at youtube", "YouTube", 1080, "Video"),
            MovieVideo("321", "iso2_1", "iso2_2",
                "123456789101112", "Filme 2 at youtube", "YouTube", 1080, "Video")))

        val testSingle = Single.just(movieVideos)

        Mockito.`when`(moviesApiService.getVideosFromMovie(16.toString())).thenReturn(testSingle)

        moviesViewModel.getVideosFromMovie(16)

        Assert.assertEquals(2, moviesViewModel.videosFromMovie.value?.size)
    }

    @Test
    fun getVideosFromMovieFailure() {
        val testSingle = Single.error<MovieVideos>(Throwable())
        Mockito.`when`(moviesApiService.getVideosFromMovie(16.toString())).thenReturn(testSingle)
        moviesViewModel.getVideosFromMovie(16)
        Assert.assertEquals(0, moviesViewModel.videosFromMovie.value?.size)
    }
}
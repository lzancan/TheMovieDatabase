package com.example.themoviedatabase.di

import com.example.themoviedatabase.model.MoviesApi
import com.example.themoviedatabase.model.MoviesApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
open class ApiModule {

    private val baseUrl = "https://api.themoviedb.org/3/"

    @Provides
    fun provideMoviesApi(): MoviesApi {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MoviesApi::class.java)
    }

    @Provides
    open fun provideMoviesApiService(): MoviesApiService {
        return MoviesApiService()
    }

}
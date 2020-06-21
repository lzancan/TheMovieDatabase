package com.example.themoviedatabase

import com.example.themoviedatabase.di.ApiModule
import com.example.themoviedatabase.model.MoviesApiService

class ApiModuleTest(val mockService: MoviesApiService): ApiModule() {
    override fun provideMoviesApiService(): MoviesApiService {
        return mockService
    }
}
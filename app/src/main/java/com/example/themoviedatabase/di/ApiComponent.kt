package com.example.themoviedatabase.di

import com.example.themoviedatabase.model.MoviesApiService
import dagger.Component

@Component(modules = [ApiModule::class])
interface ApiComponent {

    fun inject(service: MoviesApiService)
}
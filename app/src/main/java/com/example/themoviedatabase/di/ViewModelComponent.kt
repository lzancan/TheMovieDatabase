package com.example.themoviedatabase.di

import com.example.themoviedatabase.viewmodel.MoviesViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, PrefsModule::class, AppModule::class])
interface ViewModelComponent {

    fun inject(viewModel: MoviesViewModel)
}
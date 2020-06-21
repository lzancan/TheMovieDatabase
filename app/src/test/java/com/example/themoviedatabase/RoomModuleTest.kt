package com.example.themoviedatabase

import android.app.Application
import com.example.themoviedatabase.di.RoomModule
import com.example.themoviedatabase.model.MoviesDatabase

class RoomModuleTest(val mockRoom: MoviesDatabase): RoomModule() {
    override fun provideDatabase(app: Application): MoviesDatabase {
        return mockRoom
    }
}
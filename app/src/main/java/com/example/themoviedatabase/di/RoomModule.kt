package com.example.themoviedatabase.di

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.example.themoviedatabase.di.ContextModule.CONTEXT_ACTIVITY
import com.example.themoviedatabase.di.ContextModule.CONTEXT_APP
import com.example.themoviedatabase.model.MoviesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
open class RoomModule {

    @Provides
    @Singleton
    @ContextModule.TypeOfContext(CONTEXT_APP)
    open fun provideDatabase(app: Application): MoviesDatabase {
        return MoviesDatabase(app)
    }

    @Provides
    @Singleton
    @ContextModule.TypeOfContext(CONTEXT_ACTIVITY)
    fun provideActivityDatabase(activity: AppCompatActivity): MoviesDatabase {
        return MoviesDatabase(activity.applicationContext)
    }

}
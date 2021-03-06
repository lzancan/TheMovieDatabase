package com.example.themoviedatabase.di

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.example.themoviedatabase.di.ContextModule.CONTEXT_ACTIVITY
import com.example.themoviedatabase.di.ContextModule.CONTEXT_APP
import com.example.themoviedatabase.util.SharedPreferencesHelper
import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
open class PrefsModule {

    @Provides
    @Singleton
    @ContextModule.TypeOfContext(CONTEXT_APP)
    open fun provideSharedPreferences(app: Application): SharedPreferencesHelper {
        return SharedPreferencesHelper(app)
    }

    @Provides
    @Singleton
    @ContextModule.TypeOfContext(CONTEXT_ACTIVITY)
    fun provideActivitySharedPreferences(activity: AppCompatActivity): SharedPreferencesHelper {
        return SharedPreferencesHelper(activity)
    }
}
package com.example.themoviedatabase

import android.app.Application
import com.example.themoviedatabase.di.PrefsModule
import com.example.themoviedatabase.util.SharedPreferencesHelper

class PrefsModuleTest(val mockPrefs: SharedPreferencesHelper): PrefsModule() {
    override fun provideSharedPreferences(app: Application): SharedPreferencesHelper {
        return mockPrefs
    }
}
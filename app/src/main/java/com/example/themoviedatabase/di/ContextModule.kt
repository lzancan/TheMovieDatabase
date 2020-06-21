package com.example.themoviedatabase.di

import javax.inject.Qualifier

object ContextModule{
    const val CONTEXT_APP = "Application context"
    const val CONTEXT_ACTIVITY = "Activity context"

    @Qualifier
    annotation class TypeOfContext(val type: String)
}
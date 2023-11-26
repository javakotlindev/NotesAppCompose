package com.javakotlindev.notes

import android.app.Application
import com.javakotlindev.notes.presentation.core.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NotesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NotesApplication)
            modules(viewModelsModule)
        }
    }
}

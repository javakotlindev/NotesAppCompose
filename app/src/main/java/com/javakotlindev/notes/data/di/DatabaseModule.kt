package com.javakotlindev.notes.data.di

import com.javakotlindev.notes.data.local.NotesDao
import com.javakotlindev.notes.data.local.NotesDaoImpl
import com.squareup.sqldelight.android.AndroidSqliteDriver
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import uz.javakotlindev.notes.cache.NotesDatabase

val databaseModule = module {
    single {
        NotesDatabase(
            AndroidSqliteDriver(
                schema = NotesDatabase.Schema,
                context = get(),
                name = "notes.db"
            )
        )
    }
    singleOf(::NotesDaoImpl) { bind<NotesDao>() }
}

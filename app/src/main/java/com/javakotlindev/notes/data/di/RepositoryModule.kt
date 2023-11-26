package com.javakotlindev.notes.data.di

import com.javakotlindev.notes.data.repository.NotesRepositoryImpl
import com.javakotlindev.notes.domain.repository.NotesRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    singleOf(::NotesRepositoryImpl) { bind<NotesRepository>() }
}

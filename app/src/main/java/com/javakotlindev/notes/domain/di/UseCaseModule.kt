package com.javakotlindev.notes.domain.di

import com.javakotlindev.notes.domain.usecase.CheckNoteFieldsUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::CheckNoteFieldsUseCase)
}

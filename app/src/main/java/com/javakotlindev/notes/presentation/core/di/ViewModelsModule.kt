package com.javakotlindev.notes.presentation.core.di

import com.javakotlindev.notes.presentation.screens.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelsModule = module {
    viewModelOf(::HomeViewModel)
}

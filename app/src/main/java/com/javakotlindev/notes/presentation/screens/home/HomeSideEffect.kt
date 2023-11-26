package com.javakotlindev.notes.presentation.screens.home

sealed interface HomeSideEffect {
    data object OpenSearchScreen : HomeSideEffect
}

package com.javakotlindev.notes.presentation.screens.note

sealed interface NoteSideEffect {
    data object PopUp : NoteSideEffect
}
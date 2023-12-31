package com.javakotlindev.notes.presentation.screens.home

import com.javakotlindev.notes.domain.model.NoteModel

sealed interface HomeSideEffect {
    data class OpenNoteScreen(val note: NoteModel) : HomeSideEffect
}

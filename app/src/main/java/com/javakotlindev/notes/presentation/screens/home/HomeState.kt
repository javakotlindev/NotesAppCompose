package com.javakotlindev.notes.presentation.screens.home

import com.javakotlindev.notes.domain.model.NoteModel

data class HomeState(
    val notes: List<NoteModel> = emptyList()
)

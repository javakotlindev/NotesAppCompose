package com.javakotlindev.notes.presentation.screens.search

import com.javakotlindev.notes.domain.model.NoteModel

data class SearchState(
    val notes: List<NoteModel> = emptyList(),
    val query: String = ""
)

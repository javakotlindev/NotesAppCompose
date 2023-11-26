package com.javakotlindev.notes.presentation.screens.home

import com.javakotlindev.notes.domain.model.NoteColor.RED
import com.javakotlindev.notes.domain.model.NoteModel

data class HomeState(
    val notes: List<NoteModel> = listOf(NoteModel(1, "", "", RED))
)

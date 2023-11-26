package com.javakotlindev.notes.presentation.screens.note

import com.javakotlindev.notes.domain.model.NoteColor
import com.javakotlindev.notes.domain.model.NoteColor.RED

data class NoteState(
    val isReadMode: Boolean = false,
    val title: String = "",
    val description: String = "",
    val color: NoteColor = RED,
    val isSaveEnabled: Boolean = false
)

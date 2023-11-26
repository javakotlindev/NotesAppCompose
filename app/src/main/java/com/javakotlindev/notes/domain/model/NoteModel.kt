package com.javakotlindev.notes.domain.model

data class NoteModel(
    val id: Int,
    val title: String,
    val description: String,
    val color: NoteColor,
)

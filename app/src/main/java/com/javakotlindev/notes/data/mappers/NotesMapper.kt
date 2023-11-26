package com.javakotlindev.notes.data.mappers

import com.javakotlindev.notes.domain.model.NoteColor
import com.javakotlindev.notes.domain.model.NoteModel
import database.NotesEntity

fun NotesEntity.mapToNoteModel() = NoteModel(
    id = id,
    title = title,
    description = description,
    color = NoteColor.getSafeColor(color)
)

fun NoteModel.mapToNoteEntity() = NotesEntity(
    id = id,
    title = title,
    description = description,
    color = color.name
)

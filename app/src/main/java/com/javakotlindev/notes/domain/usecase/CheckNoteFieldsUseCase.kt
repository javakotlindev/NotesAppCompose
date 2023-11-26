package com.javakotlindev.notes.domain.usecase

import com.javakotlindev.notes.domain.model.NoteColor
import com.javakotlindev.notes.domain.model.NoteModel

class CheckNoteFieldsUseCase {

    operator fun invoke(
        title: String,
        description: String,
        color: NoteColor,
        previousNote: NoteModel
    ): Boolean {
        if (title.trim().isEmpty() && description.trim().isEmpty()) return false
        if (title.length < 5 || description.length < 10) return false
        return previousNote.title != title.trim() ||
                previousNote.description != description.trim() ||
                previousNote.color != color
    }
}
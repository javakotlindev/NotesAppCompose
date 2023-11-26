package com.javakotlindev.notes.domain.repository

import com.javakotlindev.notes.domain.model.NoteModel
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getAllNotes(): Flow<List<NoteModel>>

    suspend fun saveNote(noteModel: NoteModel)
}

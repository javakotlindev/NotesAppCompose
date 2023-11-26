package com.javakotlindev.notes.data.local

import database.NotesEntity
import kotlinx.coroutines.flow.Flow

interface NotesDao {
    fun getAllNotes(): Flow<List<NotesEntity>>

    suspend fun saveNote(notesEntity: NotesEntity)

    suspend fun deleteNote(id: String)
}
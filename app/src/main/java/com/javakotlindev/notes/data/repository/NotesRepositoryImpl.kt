package com.javakotlindev.notes.data.repository

import com.javakotlindev.notes.data.local.NotesDao
import com.javakotlindev.notes.data.mappers.mapToNoteEntity
import com.javakotlindev.notes.data.mappers.mapToNoteModel
import com.javakotlindev.notes.domain.model.NoteModel
import com.javakotlindev.notes.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl(
    private val notesDao: NotesDao
) : NotesRepository {
    override fun getAllNotes(): Flow<List<NoteModel>> {
        return notesDao.getAllNotes().map { notes -> notes.map { note -> note.mapToNoteModel() } }
    }

    override suspend fun saveNote(noteModel: NoteModel) {
        notesDao.saveNote(noteModel.mapToNoteEntity())
    }
}

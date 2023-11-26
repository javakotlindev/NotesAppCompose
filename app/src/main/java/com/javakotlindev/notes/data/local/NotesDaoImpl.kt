package com.javakotlindev.notes.data.local

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import database.NotesEntity
import uz.javakotlindev.notes.cache.NotesDatabase

class NotesDaoImpl(
    private val database: NotesDatabase
) : NotesDao {

    override fun getAllNotes() = database.notesQueries.getAllNotes().asFlow().mapToList()

    override suspend fun saveNote(notesEntity: NotesEntity) {
        database.notesQueries.saveNote(notesEntity)
    }
}
package com.javakotlindev.notes.domain.usecase

import com.javakotlindev.notes.domain.model.NoteModel
import com.javakotlindev.notes.domain.repository.NotesRepository
import com.javakotlindev.notes.presentation.core.utils.withIOContext
import kotlinx.coroutines.flow.firstOrNull

class SearchNotesUseCase(
    private val repository: NotesRepository
) {

    suspend operator fun invoke(query: String): List<NoteModel> {
        return withIOContext {
            repository.getAllNotes().firstOrNull().orEmpty().filter { note ->
                note.title.uppercase().contains(query.uppercase()) or
                        note.description.uppercase().contains(query.uppercase())
            }
        }
    }
}

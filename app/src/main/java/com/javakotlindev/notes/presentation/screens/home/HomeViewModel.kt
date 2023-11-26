package com.javakotlindev.notes.presentation.screens.home

import com.javakotlindev.notes.domain.model.NoteModel
import com.javakotlindev.notes.domain.repository.NotesRepository
import com.javakotlindev.notes.presentation.core.utils.BaseViewModel
import com.javakotlindev.notes.presentation.screens.home.HomeSideEffect.OpenNoteScreen
import kotlinx.coroutines.launch

class HomeViewModel(
    private val notesRepository: NotesRepository
) : BaseViewModel<HomeState, HomeSideEffect>(HomeState()) {

    init {
        launch {
            notesRepository.getAllNotes().collect { notes ->
                reduce { state -> state.copy(notes = notes) }
            }
        }
    }

    fun onAddNote() {
        launch { postSideEffect(OpenNoteScreen(NoteModel())) }
    }
}

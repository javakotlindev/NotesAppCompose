package com.javakotlindev.notes.presentation.screens.search

import com.javakotlindev.notes.domain.repository.NotesRepository
import com.javakotlindev.notes.domain.usecase.SearchNotesUseCase
import com.javakotlindev.notes.presentation.core.utils.BaseViewModel
import kotlinx.coroutines.launch

class SearchViewModel(
    private val notesRepository: NotesRepository,
    private val searchNotesUseCase: SearchNotesUseCase
) : BaseViewModel<SearchState, Unit>(SearchState()) {

    init {
        launch {
            notesRepository.getAllNotes().collect { notes ->
                reduce { state -> state.copy(notes = notes) }
            }
        }
    }

    fun onSearch(query: String) {
        launch {
            reduce { state -> state.copy(query = query) }
            searchNotesUseCase(query).let { reduce { state -> state.copy(notes = it) } }
        }
    }
}

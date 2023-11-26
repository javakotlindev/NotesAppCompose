package com.javakotlindev.notes.presentation.screens.note

import com.javakotlindev.notes.domain.model.NoteColor
import com.javakotlindev.notes.domain.model.NoteModel
import com.javakotlindev.notes.domain.repository.NotesRepository
import com.javakotlindev.notes.domain.usecase.CheckNoteFieldsUseCase
import com.javakotlindev.notes.presentation.core.utils.BaseViewModel
import com.javakotlindev.notes.presentation.screens.note.NoteSideEffect.PopUp
import kotlinx.coroutines.launch
import java.util.UUID

class NoteViewModel(
    private val note: NoteModel,
    private val checkNoteUseCase: CheckNoteFieldsUseCase,
    private val notesRepository: NotesRepository
) : BaseViewModel<NoteState, NoteSideEffect>(
    NoteState(
        isReadMode = note.id.isNotEmpty(),
        title = note.title,
        description = note.description,
        color = note.color
    )
) {

    fun onChangeTitle(title: String) {
        launch { reduce { state -> state.copy(title = title) } }
        onValidateSave()
    }

    fun onChangeDesc(desc: String) {
        launch { reduce { state -> state.copy(description = desc) } }
        onValidateSave()
    }

    fun onSelectColor(color: NoteColor) {
        launch { reduce { state -> state.copy(color = color) } }
        onValidateSave()
    }

    fun onReadModeSelect(isRead: Boolean) {
        launch {
            reduce { state -> state.copy(isReadMode = isRead) }
        }
    }

    fun onSaveNote() {
        launch {
            notesRepository.saveNote(
                NoteModel(
                    id = UUID.randomUUID().toString(),
                    title = state.title.trim(),
                    description = state.description.trim(),
                    color = state.color
                )
            )
            postSideEffect(PopUp)
        }
    }

    private fun onValidateSave() {
        launch {
            val isSaveEnabled = checkNoteUseCase(state.title, state.description, state.color, note)
            reduce { state -> state.copy(isSaveEnabled = isSaveEnabled) }
        }
    }
}
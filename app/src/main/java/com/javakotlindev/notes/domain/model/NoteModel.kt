package com.javakotlindev.notes.domain.model

import android.os.Parcelable
import com.javakotlindev.notes.domain.model.NoteColor.RED
import kotlinx.parcelize.Parcelize

@Parcelize
data class NoteModel(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val color: NoteColor = RED,
) : Parcelable

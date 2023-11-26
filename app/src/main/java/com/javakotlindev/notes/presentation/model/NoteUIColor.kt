package com.javakotlindev.notes.presentation.model

import androidx.compose.ui.graphics.Color
import com.javakotlindev.notes.presentation.ui.theme.BlueAqua
import com.javakotlindev.notes.presentation.ui.theme.Pink

enum class NoteUIColor(val color: Color) {
    RED(Color.Red), PINK(Pink), YELLOW(Color.Yellow), AQUA(BlueAqua);

    companion object {
        // Getting colors safely without crash if doesn't exist
        fun getSafeColor(type: String) = try {
            NoteUIColor.valueOf(type)
        } finally {
            RED
        }
    }
}

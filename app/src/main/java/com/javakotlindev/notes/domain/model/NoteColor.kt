package com.javakotlindev.notes.domain.model

enum class NoteColor {
    RED, PINK, YELLOW, AQUA;

    companion object {
        fun getSafeColor(type: String) = try {
            NoteColor.valueOf(type)
        } finally {
            RED
        }
    }
}

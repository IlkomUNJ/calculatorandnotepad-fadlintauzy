package com.example.kakka

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp


data class Note(
    val id: Int,
    val annotatedContent: AnnotatedString,
) {
    val content: String get() = annotatedContent.text
}

fun String.toDefaultAnnotatedString(): AnnotatedString = buildAnnotatedString {
    append(this@toDefaultAnnotatedString)
}



object NoteManager {
    var notes: SnapshotStateList<Note> = mutableStateListOf()


    fun getNoteById(id: Int): Note? = notes.find { it.id == id }


}


class EditNoteStateHolder(
    initialNote: Note?,
    val defaultStyle: SpanStyle = SpanStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal, fontStyle = FontStyle.Normal)
) {
    var noteTextFieldValue by mutableStateOf(
        TextFieldValue(
            annotatedString = initialNote?.annotatedContent ?: "".toDefaultAnnotatedString(),
            selection = TextRange(initialNote?.content?.length ?: 0)
        )
    )

    var currentTextStyle by mutableStateOf(
        initialNote?.annotatedContent?.let { annotated ->
            if (annotated.text.isNotEmpty()) {
                val lastCharIndex = annotated.text.length.coerceAtLeast(1) - 1
                annotated.spanStyles.find { lastCharIndex in it.start until it.end }?.item ?: defaultStyle
            } else {
                defaultStyle
            }
        } ?: defaultStyle
    )

    fun addNote() {
        noteTextFieldValue = TextFieldValue(
            annotatedString = "".toDefaultAnnotatedString(),
            selection = TextRange(0)
        )

        // 2. Reset style saat ini kembali ke default.
        currentTextStyle = defaultStyle
    }
    fun applyStyleToSelection(newStyle: SpanStyle) {
        val selection = noteTextFieldValue.selection
        val oldAnnotatedString = noteTextFieldValue.annotatedString

        if (selection.collapsed.not()) {
            val selectedText = oldAnnotatedString.subSequence(selection.start, selection.end)

            val newAnnotatedString = buildAnnotatedString {
                append(oldAnnotatedString.subSequence(0, selection.start))

                val mergedStyle = selectedText.spanStyles.firstOrNull()?.item?.merge(newStyle) ?: newStyle

                withStyle(mergedStyle) {
                    append(selectedText.text)
                }

                if (selection.end < oldAnnotatedString.text.length) {
                    append(oldAnnotatedString.subSequence(selection.end, oldAnnotatedString.text.length))
                }
            }

            noteTextFieldValue = noteTextFieldValue.copy(annotatedString = newAnnotatedString, selection = selection)
        }

        currentTextStyle = newStyle
    }

    fun onTextFieldValueChange(newValue: TextFieldValue) {
        val oldText = noteTextFieldValue.text
        val newText = newValue.text
        val selection = newValue.selection
        val oldAnnotatedString = noteTextFieldValue.annotatedString

        var newAnnotatedString = oldAnnotatedString

        if (newText.length > oldText.length) {
            val diff = newText.length - oldText.length
            val start = selection.start - diff
            val insertedText = newText.substring(start, selection.start)

            newAnnotatedString = buildAnnotatedString {
                append(oldAnnotatedString.subSequence(0, start))
                withStyle(currentTextStyle) {
                    append(insertedText)
                }
                if (start < oldText.length) {
                    append(oldAnnotatedString.subSequence(start, oldText.length))
                }
            }
        } else if (newText.length < oldText.length) {
            val deletedRangeStart = selection.start
            val deletedRangeEnd = selection.start + (oldText.length - newText.length)

            newAnnotatedString = buildAnnotatedString {
                append(oldAnnotatedString.subSequence(0, deletedRangeStart))
                if (deletedRangeEnd < oldText.length) {
                    append(oldAnnotatedString.subSequence(deletedRangeEnd, oldText.length))
                }
            }
        } else {
            newAnnotatedString = oldAnnotatedString
        }

        noteTextFieldValue = newValue.copy(annotatedString = newAnnotatedString)

        val cursorPosition = newValue.selection.end.coerceAtMost(newAnnotatedString.text.length)

        if (newAnnotatedString.text.isNotEmpty()) {
            currentTextStyle = if (newValue.selection.collapsed) {
                newAnnotatedString.spanStyles
                    .find { cursorPosition in it.start..it.end }?.item ?: defaultStyle
            } else {
                newAnnotatedString.spanStyles
                    .find { newValue.selection.start in it.start..it.end }?.item ?: defaultStyle
            }
        } else {
            currentTextStyle = defaultStyle
        }
    }


}

@Composable
fun rememberEditNoteStateHolder(noteId: Int): EditNoteStateHolder {
    val existingNote = NoteManager.getNoteById(noteId)
    return remember(noteId) {
        EditNoteStateHolder(initialNote = existingNote)
    }
}
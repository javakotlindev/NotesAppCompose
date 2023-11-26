package com.javakotlindev.notes.presentation.screens.note

import android.os.Parcelable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.javakotlindev.notes.R.drawable
import com.javakotlindev.notes.domain.model.NoteColor
import com.javakotlindev.notes.domain.model.NoteModel
import com.javakotlindev.notes.presentation.core.utils.collectSideEffect
import com.javakotlindev.notes.presentation.model.NoteUIColor
import com.javakotlindev.notes.presentation.screens.note.NoteSideEffect.PopUp
import kotlinx.parcelize.Parcelize
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Parcelize
data class NoteScreen(val note: NoteModel) : Screen, Parcelable {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<NoteViewModel> { parametersOf(note) }
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        var isShowConfirm by remember { mutableStateOf(false) }
        if (isShowConfirm) {
            SaveDialog(
                onDismiss = { isShowConfirm = false },
                onConfirm = {
                    viewModel.onSaveNote()
                    isShowConfirm = false
                }
            )
        }
        NoteContent(
            uiState = uiState,
            onPopUp = navigator::pop,
            onChangeTitle = viewModel::onChangeTitle,
            onChangeDesc = viewModel::onChangeDesc,
            onSelectColor = viewModel::onSelectColor,
            onReadSelect = viewModel::onReadModeSelect,
            onSaveNote = { isShowConfirm = true }
        )
        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                PopUp -> navigator.pop()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun NoteContent(
        uiState: NoteState,
        onPopUp: () -> Unit,
        onChangeTitle: (String) -> Unit,
        onChangeDesc: (String) -> Unit,
        onSelectColor: (NoteColor) -> Unit,
        onReadSelect: (Boolean) -> Unit,
        onSaveNote: () -> Unit
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = note.title) },
                    navigationIcon = {
                        IconButton(onClick = onPopUp) {
                            Image(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                    },
                    actions = {
                        AnimatedContent(targetState = uiState.isReadMode, label = "") { isRead ->
                            if (isRead) {
                                IconButton(onClick = { onReadSelect(false) }) {
                                    Image(Icons.Default.Edit, null)
                                }
                            } else if (note.id.isNotEmpty()) {
                                IconButton(onClick = { onReadSelect(true) }) {
                                    Image(painterResource(drawable.visibility), null)
                                }
                            }
                        }
                        AnimatedVisibility(uiState.isSaveEnabled) {
                            IconButton(onClick = onSaveNote) {
                                Image(
                                    painter = painterResource(drawable.save),
                                    contentDescription = null
                                )
                            }
                        }
                    }
                )
            }
        ) {
            Column(modifier = Modifier.padding(it)) {
                TextField(
                    value = uiState.title,
                    onValueChange = onChangeTitle,
                    placeholder = { Text(text = "Title", fontSize = 48.sp) },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent,
                        disabledIndicatorColor = Transparent,
                        focusedContainerColor = Transparent,
                        unfocusedContainerColor = Transparent
                    ),
                    textStyle = TextStyle(fontSize = 48.sp),
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = uiState.isReadMode
                )
                TextField(
                    value = uiState.description,
                    onValueChange = onChangeDesc,
                    placeholder = { Text(text = "Type something...", fontSize = 23.sp) },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent,
                        disabledIndicatorColor = Transparent,
                        focusedContainerColor = Transparent,
                        unfocusedContainerColor = Transparent
                    ),
                    textStyle = TextStyle(fontSize = 23.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    readOnly = uiState.isReadMode
                )
                LazyRow(
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    items(NoteUIColor.entries) { noteColor ->
                        val borderColor by animateColorAsState(
                            targetValue = if (uiState.color.name == noteColor.name) White else Transparent,
                            label = ""
                        )
                        Box(
                            modifier = Modifier
                                .padding(6.dp)
                                .size(45.dp)
                                .border(width = 1.dp, color = borderColor, shape = CircleShape)
                                .padding(2.dp)
                                .clip(CircleShape)
                                .background(noteColor.color)
                                .clickable { onSelectColor(NoteColor.getSafeColor(noteColor.name)) }
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun SaveDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
        AlertDialog(onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, MaterialTheme.shapes.medium)
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(imageVector = Icons.Default.Info, contentDescription = null)
                Spacer(modifier = Modifier.height(20.dp))
                Text(text = "Save changes ?", fontSize = 23.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Discard")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "Keep")
                    }
                }
            }
        }
    }
}

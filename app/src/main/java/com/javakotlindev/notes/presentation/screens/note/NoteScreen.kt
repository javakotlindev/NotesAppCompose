package com.javakotlindev.notes.presentation.screens.note

import android.os.Parcelable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.javakotlindev.notes.R.drawable
import com.javakotlindev.notes.R.string
import com.javakotlindev.notes.domain.model.NoteColor
import com.javakotlindev.notes.domain.model.NoteModel
import com.javakotlindev.notes.presentation.core.utils.collectSideEffect
import com.javakotlindev.notes.presentation.model.NoteUIColor
import com.javakotlindev.notes.presentation.screens.note.NoteSideEffect.PopUp
import com.javakotlindev.notes.presentation.ui.theme.NotesTheme
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
    fun NoteContent(
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
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    },
                    actions = {
                        AnimatedContent(targetState = uiState.isReadMode, label = "") { isRead ->
                            if (isRead) {
                                IconButton(
                                    onClick = { onReadSelect(false) },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            } else if (note.id.isNotEmpty()) {
                                IconButton(
                                    onClick = { onReadSelect(true) },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                    )
                                ) {
                                    Icon(
                                        painterResource(drawable.visibility),
                                        null,
                                        tint = MaterialTheme.colorScheme.onSecondary
                                    )
                                }
                            }
                        }
                        AnimatedVisibility(uiState.isSaveEnabled) {
                            IconButton(onClick = onSaveNote) {
                                Icon(
                                    painter = painterResource(drawable.save),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
            ) {
                TextField(
                    value = uiState.title,
                    onValueChange = onChangeTitle,
                    placeholder = { Text(text = stringResource(string.Title), fontSize = 48.sp) },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent,
                        disabledIndicatorColor = Transparent,
                        focusedContainerColor = Transparent,
                        unfocusedContainerColor = Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 48.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = uiState.isReadMode
                )
                TextField(
                    value = uiState.description,
                    onValueChange = onChangeDesc,
                    placeholder = {
                        Text(text = stringResource(string.DescHint), fontSize = 23.sp)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent,
                        disabledIndicatorColor = Transparent,
                        focusedContainerColor = Transparent,
                        unfocusedContainerColor = Transparent
                    ),
                    textStyle = TextStyle(
                        fontSize = 23.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    readOnly = uiState.isReadMode
                )
                LazyRow(
                    modifier = Modifier
                        .padding(vertical = 12.dp)
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
                Text(text = stringResource(string.SaveChanges), fontSize = 23.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Row {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(string.Discard))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(string.Keep))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ShowNotes() {
    NotesTheme {
        NoteScreen(NoteModel()).NoteContent(
            uiState = NoteState(),
            onPopUp = { },
            onChangeTitle = {},
            onChangeDesc = {},
            onSelectColor = {},
            onReadSelect = {},
            onSaveNote = {}
        )
    }
}

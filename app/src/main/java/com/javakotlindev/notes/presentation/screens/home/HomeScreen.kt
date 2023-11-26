package com.javakotlindev.notes.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.javakotlindev.notes.R
import com.javakotlindev.notes.R.string
import com.javakotlindev.notes.domain.model.NoteModel
import com.javakotlindev.notes.presentation.core.utils.collectSideEffect
import com.javakotlindev.notes.presentation.model.NoteUIColor.Companion.getSafeColor
import com.javakotlindev.notes.presentation.screens.home.HomeSideEffect.OpenNoteScreen
import com.javakotlindev.notes.presentation.screens.note.NoteScreen
import com.javakotlindev.notes.presentation.screens.search.SearchScreen
import com.javakotlindev.notes.presentation.ui.component.EmptyContent
import com.javakotlindev.notes.presentation.ui.theme.NotesTheme
import org.koin.androidx.compose.koinViewModel

class HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<HomeViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        HomeContent(
            uiState = uiState,
            onAddNote = viewModel::onAddNote,
            onSearch = { navigator.push(SearchScreen()) },
            onSelectNote = { navigator.push(NoteScreen(it)) },
            onDeleteNote = viewModel::onDeleteNote
        )
        OnSideEffect(viewModel = viewModel, navigator = navigator)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun HomeContent(
        uiState: HomeState,
        onAddNote: () -> Unit,
        onSearch: () -> Unit,
        onSelectNote: (NoteModel) -> Unit,
        onDeleteNote: (id: String) -> Unit
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(string.app_name)) },
                    actions = {
                        IconButton(
                            onClick = onSearch,
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddNote,
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ) {
                    Image(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.primary,
        ) { paddings ->
            Box(
                modifier = Modifier
                    .padding(paddings)
                    .fillMaxSize()
            ) {
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(uiState.notes, key = { it.id }) { note ->
                        val dismissState = rememberDismissState()
                        if (dismissState.isDismissed(DismissDirection.EndToStart)) onDeleteNote(note.id)
                        NoteItem(note, dismissState, onSelectNote)
                    }
                }
                AnimatedVisibility(
                    uiState.notes.isEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut(),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    EmptyContent(
                        modifier = Modifier.padding(20.dp),
                        painter = painterResource(id = R.drawable.ic_empty),
                        text = stringResource(string.CreateFirstNote)
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun NoteItem(
        note: NoteModel,
        dismissState: DismissState,
        onSelectNote: (NoteModel) -> Unit
    ) {
        SwipeToDismiss(
            state = dismissState,
            directions = setOf(DismissDirection.EndToStart),
            background = {
                val backgroundColor by animateColorAsState(
                    when (dismissState.targetValue) {
                        DismissValue.DismissedToStart -> Color.Red.copy(alpha = 0.8f)
                        else -> Color.Transparent
                    },
                    label = ""
                )
                val iconScale by animateFloatAsState(
                    targetValue = if (dismissState.targetValue == DismissValue.DismissedToStart) 1.3f else 0.5f,
                    label = ""
                )
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(color = backgroundColor, MaterialTheme.shapes.small)
                        .padding(end = 16.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Icon(
                        modifier = Modifier.scale(iconScale),
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            },
            dismissContent = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.small)
                        .clickable { onSelectNote(note) }
                        .background(color = getSafeColor(note.color.name).color)
                        .padding(20.dp),
                ) {
                    Text(text = note.title, color = Color.White)
                }
            }
        )
    }

    @Composable
    private fun OnSideEffect(viewModel: HomeViewModel, navigator: Navigator) {
        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is OpenNoteScreen -> navigator.push(NoteScreen(sideEffect.note))
            }
        }
    }

    @Preview
    @Composable
    private fun ShowHome() {
        NotesTheme {
            HomeContent(
                uiState = HomeState(),
                onAddNote = {},
                onSearch = {},
                onSelectNote = {},
                onDeleteNote = {}
            )
        }
    }
}
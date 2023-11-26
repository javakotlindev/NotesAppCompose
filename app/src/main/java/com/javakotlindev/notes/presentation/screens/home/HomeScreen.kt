package com.javakotlindev.notes.presentation.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.javakotlindev.notes.R
import com.javakotlindev.notes.presentation.core.utils.collectSideEffect
import com.javakotlindev.notes.presentation.model.NoteUIColor.Companion.getSafeColor
import com.javakotlindev.notes.presentation.screens.home.HomeSideEffect.OpenNoteScreen
import com.javakotlindev.notes.presentation.screens.home.HomeSideEffect.OpenSearchScreen
import com.javakotlindev.notes.presentation.screens.note.NoteScreen
import com.javakotlindev.notes.presentation.ui.theme.NotesTheme
import org.koin.androidx.compose.koinViewModel

object HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<HomeViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        HomeContent(uiState, onAddNote = viewModel::onAddNote, onSearch = {})
        OnSideEffect(viewModel = viewModel, navigator = navigator)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun HomeContent(uiState: HomeState, onAddNote: () -> Unit, onSearch: () -> Unit) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Notes") },
                    actions = {
                        IconButton(onClick = onSearch) {
                            Image(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Image(
                                imageVector = Icons.Default.Info,
                                contentDescription = null
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = onAddNote) {
                    Image(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                }
            }
        ) { paddings ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
            ) {
                LazyColumn(contentPadding = PaddingValues(16.dp)) {
                    items(uiState.notes) { note ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = getSafeColor(note.color.name).color,
                                    shape = MaterialTheme.shapes.small
                                )
                                .padding(20.dp),
                        ) {
                            Text(text = note.title)
                        }
                    }
                }
                if (uiState.notes.isEmpty()) {
                    EmptyNotes(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(20.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun EmptyNotes(modifier: Modifier) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_empty),
                contentDescription = null,
                modifier = Modifier.aspectRatio(7f / 5f),
            )
            Text(text = "Create your first note !")
        }
    }

    @Composable
    private fun OnSideEffect(viewModel: HomeViewModel, navigator: Navigator) {
        viewModel.collectSideEffect { sideEffect ->
            when (sideEffect) {
                is OpenNoteScreen -> navigator.push(NoteScreen(sideEffect.note))
                OpenSearchScreen -> {

                }
            }
        }
    }

    @Preview
    @Composable
    private fun ShowHome() {
        NotesTheme {
            HomeContent(uiState = HomeState(), onAddNote = {}, onSearch = {})
        }
    }
}
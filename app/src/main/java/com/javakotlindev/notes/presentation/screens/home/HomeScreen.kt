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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import com.javakotlindev.notes.R
import com.javakotlindev.notes.presentation.model.NoteUIColor.Companion.getSafeColor
import org.koin.androidx.compose.koinViewModel

object HomeScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<HomeViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        HomeContent(uiState)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun HomeContent(uiState: HomeState) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Notes") },
                    actions = {
                        IconButton(onClick = { /*TODO*/ }) {
                            Image(
                                imageVector = Icons.Default.Search,
                                contentDescription = "SearchIcon"
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Image(
                                imageVector = Icons.Default.Info,
                                contentDescription = "InfoIcon"
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { /*TODO*/ }) {
                    Image(imageVector = Icons.Default.Add, contentDescription = null)
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
}
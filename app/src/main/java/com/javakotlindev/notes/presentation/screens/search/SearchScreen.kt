package com.javakotlindev.notes.presentation.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.javakotlindev.notes.R
import com.javakotlindev.notes.domain.model.NoteModel
import com.javakotlindev.notes.presentation.model.NoteUIColor
import com.javakotlindev.notes.presentation.screens.note.NoteScreen
import com.javakotlindev.notes.presentation.ui.component.EmptyContent
import org.koin.androidx.compose.koinViewModel

object SearchScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = koinViewModel<SearchViewModel>()
        val uiState by viewModel.uiState.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        SearchContent(
            uiState = uiState,
            onPopUp = navigator::pop,
            onSearch = viewModel::onSearch,
            onSelectItem = { navigator.replace(NoteScreen(it)) }
        )
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    private fun SearchContent(
        uiState: SearchState,
        onPopUp: () -> Unit,
        onSearch: (String) -> Unit,
        onSelectItem: (NoteModel) -> Unit
    ) {
        Scaffold(
            topBar = {
                LargeTopAppBar(
                    title = {
                        TextField(
                            value = uiState.query,
                            onValueChange = onSearch,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 20.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                            ),
                            shape = MaterialTheme.shapes.large,
                        )
                    },
                    scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                    navigationIcon = {
                        IconButton(onClick = onPopUp) {
                            Image(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        ) { paddings ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddings)
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.animateContentSize()
                ) {
                    items(uiState.notes) { note ->
                        Column(
                            modifier = Modifier
                                .animateItemPlacement()
                                .padding(vertical = 4.dp)
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.small)
                                .clickable { onSelectItem(note) }
                                .background(color = NoteUIColor.getSafeColor(note.color.name).color)
                                .padding(20.dp),
                        ) {
                            Text(text = note.title, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = note.description, fontSize = 14.sp, maxLines = 1)
                        }
                    }
                }
                AnimatedVisibility(
                    visible = uiState.notes.isEmpty() && uiState.query.isNotEmpty(),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    EmptyContent(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(20.dp),
                        painter = painterResource(id = R.drawable.ic_empty_search),
                        text = "File not found. Try searching again."
                    )
                }
            }
        }
    }
}

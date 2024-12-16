package com.example.spellsbook.app.ui.compose.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.spellsbook.R
import com.example.spellsbook.app.ui.compose.AddButtonShape
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.app.ui.compose.navigation.navigate
import com.example.spellsbook.domain.model.BookModel
import com.example.spellsbook.domain.usecase.AddBookUseCase
import com.example.spellsbook.domain.usecase.GetAllBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import  androidx.compose.foundation.lazy.items

@HiltViewModel
class BooksViewModel @Inject constructor(
    getAllBooksUseCase: GetAllBooksUseCase,
    addBookUseCase: AddBookUseCase // todo remove when be test databases
) : ViewModel() {
    data class State(
        val books: List<BookModel> = emptyList(),
        val isAddDialogShowing: Boolean = false,
        val bookModelForRemoveDialog: BookModel? = null,
    )

    sealed class Event {
        object ShowAddBookDialog : Event()
        object CloseAddBookDialog : Event()
        class ShowRemoveBookDialog(val model: BookModel) : Event()
        object CloseRemoveBookDialog : Event()
    }

    private val books = getAllBooksUseCase
        .execute()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )

    private val _state = MutableStateFlow(State())
    val state = combine(_state, books) { state, books ->
        state.copy(books = books)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), State())

    init { // todo remove when be test databases
        viewModelScope.launch {
            println("init")
            listOf(
                BookModel(name = "Book 1"),
                BookModel(name = "Book 2"),
                BookModel(name = "Book 3"),
                BookModel(name = "Book 4"),
            ).forEach { addBookUseCase.execute(it) }

        }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.ShowAddBookDialog -> {
                _state.value = _state.value.copy(isAddDialogShowing = true)
            }

            is Event.CloseAddBookDialog -> {
                _state.value = _state.value.copy(isAddDialogShowing = false)
            }

            is Event.ShowRemoveBookDialog -> {
                _state.value = _state.value.copy(bookModelForRemoveDialog = event.model)
            }

            is Event.CloseRemoveBookDialog -> {
                _state.value = _state.value.copy(bookModelForRemoveDialog = null)
            }
        }
    }
}


@Composable
fun BooksScreen(
    navController: NavHostController,
    viewModel: BooksViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        floatingActionButton = {
            AddFloatingButton { viewModel.onEvent(BooksViewModel.Event.ShowAddBookDialog) }
        },

        content = { padding ->
            BookList(
                navController = navController,
                padding = padding,
                viewModel = viewModel
            )
        }
    )

    if (state.isAddDialogShowing)
        AddBookDialog(
            onClose = { viewModel.onEvent(BooksViewModel.Event.CloseAddBookDialog) }
        )

    if (state.bookModelForRemoveDialog != null)
        RemoveBookDialog(
            bookModel = state.bookModelForRemoveDialog!!,
            onClose = { viewModel.onEvent(BooksViewModel.Event.CloseRemoveBookDialog) }
        )
}

@Composable
fun BookList(
    navController: NavHostController,
    padding: PaddingValues,
    viewModel: BooksViewModel
) {
    val state by viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier.padding(padding),
        contentPadding = PaddingValues(4.dp)
    ) {
        this@LazyColumn.items(state.books) { elem ->
            BookItem(
                model = elem,
                onRemove = { viewModel.onEvent(BooksViewModel.Event.ShowRemoveBookDialog(elem)) },
                navigate = { navController.navigate(NavEndpoint.BookById(elem.id)) }
            )
        }
    }
}

@Composable
fun BookItem(
    model: BookModel,
    onRemove: () -> Unit,
    navigate: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(
                horizontal = 4.dp,
                vertical = 8.dp,
            )
            .fillMaxSize(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.LightGray,
        ),
        onClick = navigate
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp,
            ),
            color = Color.Black,
            textAlign = TextAlign.Start,
            text = model.name,
            fontSize = 24.sp
        )

        IconButton(onClick = onRemove) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete button")
        }
    }
}

@Composable
fun AddFloatingButton(
    onClick: () -> Unit,
) {
    IconButton(
        modifier = AddButtonShape()
            .padding(8.dp)
            .size(60.dp),
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add_24),
            contentDescription = "Add button",
            modifier = Modifier
                .size(48.dp),
        )
    }
}


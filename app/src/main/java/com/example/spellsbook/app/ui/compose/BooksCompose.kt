package com.example.spellsbook.app.ui.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.app.ui.compose.navigation.navigate
import com.example.spellsbook.domain.model.BookModel
import com.example.spellsbook.domain.usecase.AddBookUseCase
import com.example.spellsbook.domain.usecase.GetAllBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@HiltViewModel
class BooksViewModel @Inject constructor(
    private val getAllBooksUseCase: GetAllBooksUseCase,
    private val addBookUseCase: AddBookUseCase
) : ViewModel() {
    private val _books = MutableStateFlow<List<BookModel>>(emptyList())
    val books = _books.asStateFlow()

    init { // todo delete when be test database
        runBlocking {
            listOf(
                BookModel(name = "Book 1"),
                BookModel(name = "Book 2"),
                BookModel(name = "Book 3"),
                BookModel(name = "Book 4"),
            ).forEach { addBookUseCase.execute(it) }
        }
    }

    fun executeAllBooks() {
        viewModelScope.launch {
            _books.emit(getAllBooksUseCase.execute())
            println("emit")
        }
    }
}

class BooksCompose(private val navController: NavHostController) {
    private var isDialogShowing = mutableStateOf(false)

    @Composable
    fun BooksScreen(
        viewModel: BooksViewModel = hiltViewModel(),
    ) {
        Scaffold(
            floatingActionButton = {
                AddBookFloatingButton(viewModel)
            },

            content = { padding ->
                BookList(
                    padding = padding,
                    viewModel = viewModel
                )
            }
        )
    }


    @Composable
    fun BookList(
        padding: PaddingValues,
        viewModel: BooksViewModel
    ) {
        val books by viewModel.books.collectAsState()
        val rememberIsDialogShowing by remember { isDialogShowing }

        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(4.dp)
        ) {
            if (!rememberIsDialogShowing)
                viewModel.executeAllBooks()

            println("repaint")
            items(books) { elem ->
                BookItem(model = elem)
            }
        }
    }

    @Composable
    fun BookItem(
        model: BookModel
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
            onClick = {
                println("click on item id: ${model.id}")
                navController.navigate(NavEndpoint.BookById(model.id))
            }
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
        }
    }

    @Composable
    fun AddBookFloatingButton(
        viewModel: BooksViewModel
    ) {
        var rememberIsDialogShowing by remember { isDialogShowing }

        IconButton(
            modifier = AddButtonShape()
                .padding(8.dp)
                .size(60.dp),
            onClick = { rememberIsDialogShowing = true }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add_24),
                contentDescription = "Add Book",
                modifier = Modifier
                    .size(48.dp),
            )
        }

        if (rememberIsDialogShowing)
            AddBookDialog(
                forClose = {
                    rememberIsDialogShowing = false
                },
            )
    }








}

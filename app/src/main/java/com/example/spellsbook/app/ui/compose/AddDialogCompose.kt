package com.example.spellsbook.app.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellsbook.domain.model.BookModel
import com.example.spellsbook.domain.usecase.AddBookUseCase
import com.example.spellsbook.domain.usecase.ValidateBookUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDialogViewModel @Inject constructor(
    private val validateBookUseCase: ValidateBookUseCase,
    private val addBookUseCase: AddBookUseCase
) : ViewModel() {
    data class State(
        val bookName: String,
        val bookNameError: String? = null
    )

    val state = mutableStateOf(State(""))

    fun addBookWithValidate(bookModel: BookModel) =
        validateBookUseCase.execute(bookModel).also { validationResult ->
            if (validationResult.successful) {
                addBook(bookModel)
            } else {
                state.value = state.value.copy(
                    bookNameError = validationResult.errorMessage
                )
            }
        }

    fun updateBookName(name: String) {
        state.value = state.value.copy(
            bookName = name
        )
    }


    private fun addBook(model: BookModel) {
        viewModelScope.launch {
            addBookUseCase.execute(model)
        }
    }

    fun clearState() {
        state.value = State("")
    }
}


@Composable
fun AddBookDialog(
    forClose: () -> Unit,
    viewModel: AddDialogViewModel = hiltViewModel()
) {
    val state by remember { viewModel.state }

    val onClose = {
        forClose()
        viewModel.clearState()
    }

    Dialog(onDismissRequest = onClose) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(Color.White, shape = CardDefaults.shape)
                .padding(16.dp)
                .border(20.dp, Color.White)
                .padding(20.dp)
                .wrapContentSize()
        ) {
            Text(
                text = "Add Book",
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Name",
                    fontSize = 24.sp,
                    modifier = Modifier.padding(
                        bottom = 8.dp,
                        end = 8.dp
                    )

                )
                TextField(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .height(IntrinsicSize.Min),
                    value = state.bookName,
                    onValueChange = { input -> viewModel.updateBookName(input) },
                )
            }

            Button(
                modifier = Modifier
                    .width(IntrinsicSize.Max)
                    .height(IntrinsicSize.Min),
                colors = ButtonDefaults
                    .buttonColors(
                        contentColor = Color.Gray
                    ),
                onClick = {
                    viewModel.addBookWithValidate(
                        BookModel(name = state.bookName)
                    ).also {
                        if (it.successful)
                            onClose()
                    }

                }
            ) {
                Text(text = "Add")
            }

            if (state.bookNameError != null) {
                Text(
                    text = state.bookNameError!!,
                    color = Color.Red,
                    fontSize = 16.sp
                )
            }
        }
    }
}





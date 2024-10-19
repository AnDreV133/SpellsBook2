package com.example.spellsbook.app.ui.compose

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellsbook.domain.model.BookModel
import com.example.spellsbook.domain.usecase.RemoveBookDialogUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoveBookDialogViewModel @Inject constructor(
    private val removeBookUseCase: RemoveBookDialogUseCase,
) : ViewModel() {
    sealed class Event {
        class RemoveBook(val bookModel: BookModel) : Event()
        class CloseDialog(val onClose: () -> Unit) : Event()
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.RemoveBook -> {
                viewModelScope.launch {
                    removeBookUseCase.execute(event.bookModel)
                }
            }

            is Event.CloseDialog -> {
                event.onClose()
            }
        }
    }
}

@Composable
fun RemoveBookDialog(
    bookModel: BookModel,
    onClose: () -> Unit,
    viewModel: RemoveBookDialogViewModel = hiltViewModel(),
) {
    AlertDialog(
        modifier = Modifier.wrapContentSize(),
        text = { Text("Remove «${bookModel.name}»?", fontSize = 24.sp) },
        onDismissRequest = { viewModel.onEvent(RemoveBookDialogViewModel.Event.CloseDialog(onClose)) },
        confirmButton = {
            Button(onClick = {
                viewModel.onEvent(RemoveBookDialogViewModel.Event.RemoveBook(bookModel))
                viewModel.onEvent(RemoveBookDialogViewModel.Event.CloseDialog(onClose))
            }) {
                Text("Remove", fontSize = 16.sp)
            }
        },
    )
}
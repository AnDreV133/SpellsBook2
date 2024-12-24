package com.example.spellsbook.app.ui.compose.screen

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellsbook.R
import com.example.spellsbook.app.ui.compose.export
import com.example.spellsbook.app.ui.compose.fragments.ExportToast
import com.example.spellsbook.domain.usecase.ConvertBookToJsonUseCase
import com.example.spellsbook.domain.usecase.ConvertBookToPdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class ExportBookScreenViewModel @Inject constructor(
    private val convertToJson: ConvertBookToJsonUseCase,
    private val convertToPdf: ConvertBookToPdfUseCase
) : ViewModel() {
    data class State(
        val streamData: Pair<String, InputStream>? = null,
        val isStreamGet: Boolean = false
    )

    sealed class Event {
        class ExportToJson(val bookId: Long) : Event()
        class ExportToPdf(val bookId: Long) : Event()
        object CompleteExport : Event()
    }

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun onEvent(event: Event) {
        when (event) {
            is Event.ExportToJson -> viewModelScope.launch {
                convert { convertToJson.execute(event.bookId) }
            }

            is Event.ExportToPdf -> viewModelScope.launch {
                convert { convertToPdf.execute(event.bookId) }
            }

            is Event.CompleteExport -> viewModelScope.launch {
                _state.value = State()
            }
        }
    }

    private suspend fun convert(onConvert: suspend () -> Result<Pair<String, InputStream>>) {
        onConvert().let {
            if (it.isSuccess)
                _state.value = State(streamData = it.getOrNull()!!, isStreamGet = true)
            else
                _state.value = State(streamData = null, isStreamGet = false)
        }
    }
}

@Composable
fun ExportBookScreen(
    bookId: Long,
    forClose: () -> Unit,
    viewModel: ExportBookScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    var exportResult by rememberSaveable { mutableStateOf<Boolean?>(null) }
    val context = LocalContext.current

    ExportToast(exportResult = exportResult)
    viewModel.onEvent(ExportBookScreenViewModel.Event.CompleteExport)
    exportResult = null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                viewModel.onEvent(
                    ExportBookScreenViewModel
                        .Event.ExportToJson(bookId)
                )
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(stringResource(R.string.export_to_json))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.onEvent(
                    ExportBookScreenViewModel
                        .Event.ExportToPdf(bookId)
                )
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text(stringResource(R.string.export_to_pdf))
        }
    }

    LaunchedEffect(state.value.streamData) {
        val streamData = state.value.streamData ?: return@LaunchedEffect

        exportResult = export(context, streamData)

        forClose()
    }
}



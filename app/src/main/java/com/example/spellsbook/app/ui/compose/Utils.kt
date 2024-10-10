package com.example.spellsbook.app.ui.compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellsbook.app.ui.compose.spells.SpellsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

fun <T> ViewModel.emitState(destination: MutableStateFlow<T>, state: CoroutineScope.() -> T) {
    viewModelScope.launch {
        destination.emit(
            state()
        )
    }
}
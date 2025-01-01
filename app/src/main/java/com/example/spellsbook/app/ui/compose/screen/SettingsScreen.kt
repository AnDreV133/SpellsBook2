package com.example.spellsbook.app.ui.compose.screen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.spellsbook.data.store.AppDatabaseConnection
import com.example.spellsbook.domain.usecase.IsPaidUserUseCase
import com.example.spellsbook.domain.usecase.SetupPaidUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.FileInputStream
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val setupPaidUserUseCase: SetupPaidUserUseCase,
    private val isPaidUserUseCase: IsPaidUserUseCase
) : ViewModel() {
    sealed class Event {
        data object SwitchPaidUser : Event()
        class DoDumpDB(
            val context: Context,
            val resultCallback: (Boolean) -> Unit
        ) : Event()
    }

    data class State(
        val isPaidUser: Boolean = false
    )

    private val _state = MutableStateFlow(State())
    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            State()
        )

    init {
        viewModelScope.launch {
            _state.emit(state.value.copy(isPaidUser = isPaidUserUseCase.execute()))
        }
    }

    fun onEvent(event: Event) {
        when (event) {
            is Event.SwitchPaidUser -> viewModelScope.launch {
                if (!state.value.isPaidUser)
                    setupPaidUserUseCase.enable()
                else
                    setupPaidUserUseCase.disable()

                _state.emit(state.value.copy(isPaidUser = !state.value.isPaidUser))
            }


            is Event.DoDumpDB -> viewModelScope.launch {
                event.resultCallback(
                    export(
                        event.context, Pair(
                            AppDatabaseConnection.DB_NAME,
                            FileInputStream(
                                event.context.getDatabasePath(AppDatabaseConnection.DB_NAME)
                            )
                        )
                    )
                )
            }
        }
    }
}


@Composable
fun SettingsScreen(
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = stringResource(id = R.string.paid_user))
            Switch(
                checked = state.isPaidUser,
                onCheckedChange = {
                    viewModel.onEvent(
                        SettingViewModel.Event.SwitchPaidUser
                    )
                }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            var exportResult by remember { mutableStateOf<Boolean?>(null)}
            val context = LocalContext.current

            ExportToast(exportResult = exportResult)
            exportResult = null

            Text(text = stringResource(id = R.string.dump_bd))
            Button(
                modifier = Modifier
                    .size(48.dp, 24.dp),
                onClick = {
                viewModel.onEvent(
                    SettingViewModel.Event.DoDumpDB(
                        context
                    ) { result ->
                        exportResult = result
                    }
                )
            }) { }
        }
    }
}
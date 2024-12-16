package com.example.spellsbook.app.ui.compose.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.example.spellsbook.R
import com.example.spellsbook.domain.usecase.SetupPaidUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
private class SettingViewModel @Inject constructor(
    val setupPaidUserUseCase: SetupPaidUserUseCase
) : ViewModel()

@Composable
fun SettingsScreen() {
    val viewModel = hiltViewModel<SettingViewModel>()
    var isPaidUser by remember { mutableStateOf(false) }

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
                checked = isPaidUser,
                onCheckedChange = {
                    isPaidUser = it
                    viewModel.setupPaidUserUseCase.switch(it)
                }
            )
        }
    }

    LaunchedEffect(Unit) {
        isPaidUser = viewModel.setupPaidUserUseCase.isEnable()
    }
}
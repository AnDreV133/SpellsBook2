package com.example.spellsbook.app.ui.compose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.example.spellsbook.R
import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.usecase.GetSpellDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
private class SpellDetailViewModel @Inject constructor(
    val getSpellDetailUseCase: GetSpellDetailUseCase
) : ViewModel()

@Composable
fun SpellDetailScreen(
    spellUuid: String,
) {
    val viewModel = hiltViewModel<SpellDetailViewModel>()
    var spellDetail  by remember { mutableStateOf<SpellDetailModel?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(state = rememberScrollState())
    ) {
        if (spellDetail == null) {
            Text(text = "Loading...")
        } else {
            val model = spellDetail!!
            listOf(
                R.string.detail_casting_time to model.castingTime,
                R.string.detail_range to model.range,
                R.string.detail_components to model.components,
                R.string.detail_duration to model.duration,
                R.string.detail_school to model.school,
                R.string.detail_source to model.source,
            ).forEach {
                SingleLineTextParameter(
                    stringResource(id = it.first) to it.second
                )
            }

            Text(
                modifier = Modifier
                    .padding(top = 10.dp),
                text = model.description,
                fontSize = 20.sp
            )
        }
    }

    LaunchedEffect(Unit) {
        spellDetail = viewModel.getSpellDetailUseCase.execute(spellUuid)
    }
}

@Composable
private fun SingleLineTextParameter(
    parameter: Pair<String, String>,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(text = parameter.first + ": ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        Text(text = parameter.second, fontSize = 20.sp)
    }
}
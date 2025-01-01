package com.example.spellsbook.app.ui.compose.screen

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellsbook.R
import com.example.spellsbook.app.mapper.toResString
import com.example.spellsbook.domain.enums.CastingTimeEnum
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.enums.RangeEnum
import com.example.spellsbook.domain.enums.RitualEnum
import com.example.spellsbook.domain.enums.SchoolEnum
import com.example.spellsbook.domain.enums.SourceEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.toDigit
import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.model.SpellTagsModel
import com.example.spellsbook.domain.usecase.AddUpdateSpellByAuthorUseCase
import com.example.spellsbook.domain.usecase.GetSpellDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SpellEditorViewModel @Inject constructor(
    private val addUpdateSpellByAuthorUseCase: AddUpdateSpellByAuthorUseCase,
    private val getSpellDetailUseCase: GetSpellDetailUseCase
) : ViewModel() {
    data class State(
        val name: String = "",
        val levelTag: LevelEnum? = null,
        val castingTimeTag: CastingTimeEnum? = null,
        val castingTime: String = "",
        val schoolTag: SchoolEnum? = null,
        val rangeTag: RangeEnum? = null,
        val range: String = "", // to digit else special
        val ritualTag: RitualEnum? = null,
        val description: String = "",
    )

    sealed class Event {
        class Open(val uuid: String) : Event()
        class CompleteEditing(val spell: SpellDetailModel) : Event()
        class Change(val state: State) : Event()
    }

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun onEvent(event: Event) {
        when (event) {
            is Event.Open -> {
                viewModelScope.launch {
                    getSpellDetailUseCase
                        .executeWithTagsNonLocale(event.uuid)
                        .let { (tags, spell) ->
                            _state.value = State(
                                spell.name,
                                tags.level,
                                tags.castingTime,
                                spell.castingTime,
                                tags.school,
                                tags.range,
                                spell.range,
                                tags.ritual,
                                spell.description
                            )
                        }
                }
            }

            is Event.CompleteEditing -> {
                val state = state.value
                viewModelScope.launch {
                    addUpdateSpellByAuthorUseCase.execute(
                        SpellTagsModel(
                            state.levelTag,
                            state.castingTimeTag,
                            state.schoolTag,
                            state.rangeTag,
                            SourceEnum.BY_AUTHOR,
                            state.ritualTag,
                        ),
                        event.spell
                    )
                }
            }

            is Event.Change -> {
                _state.value = event.state
            }
        }
    }
}


@Composable
fun SpellEditorScreen(
    uuid: String,
    forClose: () -> Unit,
    viewModel: SpellEditorViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val onChange = remember {
        { it: SpellEditorViewModel.State ->
            viewModel.onEvent(
                SpellEditorViewModel
                    .Event.Change(it)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .scrollable(
                rememberScrollState(),
                Orientation.Vertical
            )
    ) {
        // Name
        TextField(
            value = state.name,
            onValueChange = { onChange(state.copy(name = it)) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Level Tag
        DropdownTag(
            label = stringResource(id = R.string.tag_level_name),
            selectedItem = state.levelTag,
            items = LevelEnum.entries,
            onItemSelected = { onChange(state.copy(levelTag = it)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Casting Time Tag
        DropdownTag(
            label = stringResource(id = R.string.tag_casting_time_name),
            selectedItem = state.castingTimeTag,
            items = CastingTimeEnum.entries,
            onItemSelected = { onChange(state.copy(castingTimeTag = it)) }
        )

        Spacer(modifier = Modifier.height(8.dp))
        
        if (state.castingTimeTag?.let {
                it in listOf(CastingTimeEnum.MINUTES, CastingTimeEnum.HOURS)
            } == true
        ) {
            // Casting Time
            TextField(
                value = state.castingTime,
                onValueChange = { onChange(state.copy(castingTime = it)) },
                label = { Text("Casting Time") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
        }


        // School Tag
        DropdownTag(
            label = stringResource(id = R.string.tag_school_name),
            selectedItem = state.schoolTag,
            items = SchoolEnum.entries,
            onItemSelected = { onChange(state.copy(schoolTag = it)) }
        )

        Spacer(modifier = Modifier.height(8.dp))


        // Range Tag
        DropdownTag(
            label = stringResource(id = R.string.tag_range_name),
            selectedItem = state.rangeTag,
            items = RangeEnum.entries,
            onItemSelected = { onChange(state.copy(rangeTag = it)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (state.rangeTag?.let { it == RangeEnum.SPECIAL } == true) {
            // Range
            TextField(
                value = state.range,
                onValueChange = { onChange(state.copy(range = it)) },
                label = { Text("Range") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Ritual Tag
        DropdownTag(
            label = stringResource(id = R.string.tag_ritual_name),
            selectedItem = state.ritualTag,
            items = RitualEnum.entries,
            onItemSelected = { onChange(state.copy(ritualTag = it)) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Description
        TextField(
            value = state.description,
            onValueChange = { onChange(state.copy(description = it)) },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val model = SpellDetailModel(
                    uuid = uuid,
                    name = state.name,
                    level = state.levelTag.toDigit().toString(),
                    school = state.schoolTag?.toResString() ?: "",
                    castingTime = state.castingTimeTag?.toResString() ?: "",
                    range = state.rangeTag?.toResString() ?: "",
                    ritual = state.ritualTag?.toResString() ?: "",
                    description = state.description,
                )
            Button(
                onClick = {
                    viewModel.onEvent(SpellEditorViewModel
                        .Event.CompleteEditing(model)
                    )
                    forClose()
                }
            ) {
                Text(stringResource(id = R.string.save))
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(
            SpellEditorViewModel
                .Event.Open(uuid)
        )
    }
}

@Composable
fun <T : TagEnum> DropdownTag(
    label: String,
    selectedItem: T?,
    items: List<T>,
    onItemSelected: (T?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    TextField(
        value = selectedItem?.toResString() ?: "",
        onValueChange = {},
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { expanded = !expanded }) {
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
            }
        }
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = { Text(item.toResString()) },
                onClick = {
                    onItemSelected(item)
                    expanded = false
                }
            )
        }
    }
}
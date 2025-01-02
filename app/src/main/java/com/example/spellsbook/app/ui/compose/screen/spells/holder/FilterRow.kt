package com.example.spellsbook.app.ui.compose.screen.spells.holder

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.example.spellsbook.R
import com.example.spellsbook.app.mapper.toResString
import com.example.spellsbook.domain.enums.CastingTimeEnum
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.enums.RangeEnum
import com.example.spellsbook.domain.enums.RitualEnum
import com.example.spellsbook.domain.enums.SchoolEnum
import com.example.spellsbook.domain.enums.SourceEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

typealias FilterMap = Map<TagIdentifierEnum, List<TagEnum>>

@HiltViewModel
class FilterGridViewModel @Inject constructor() : ViewModel() {
    data class State(
        val filters: FilterMap = emptyMap(),
    )

    sealed class Event {
        class UpdateFilter(val pair: Pair<TagIdentifierEnum, List<TagEnum>>) : Event()
        object ClearFilter : Event()
    }

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun onEvent(event: Event) {
        when (event) {
            is Event.UpdateFilter -> {
                _state.value = _state.value.copy(
                    filters = _state.value.filters + event.pair
                )
            }

            is Event.ClearFilter -> {
                _state.value = State()
            }
        }
    }
}

data class FilterItemData(
    val tagIdentifier: TagIdentifierEnum,
    val tags: List<TagEnum>,
    val getTagsInFilter: () -> List<TagEnum>,
    val updateTagsInFilter: (List<TagEnum>) -> Unit
)

@Composable
fun FilterRow(
    callbackApplyFilter: (FilterMap) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FilterGridViewModel = hiltViewModel()
) {
    val filterItemDataList = remember {
        listOf(
            constructFilterItem(viewModel, TagIdentifierEnum.LEVEL, LevelEnum.entries),
            constructFilterItem(viewModel, TagIdentifierEnum.SCHOOL, SchoolEnum.entries),
            constructFilterItem(viewModel, TagIdentifierEnum.CASTING_TIME, CastingTimeEnum.entries),
            constructFilterItem(viewModel, TagIdentifierEnum.RANGE, RangeEnum.entries),
            constructFilterItem(viewModel, TagIdentifierEnum.SOURCE, SourceEnum.entries),
            constructFilterItem(viewModel, TagIdentifierEnum.RITUAL, RitualEnum.entries)
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyRow(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(filterItemDataList) {
                FilterItem(it) { callbackApplyFilter(viewModel.state.value.filters) }
            }
        }
    }
}

@Composable
fun FilterItem(
    itemData: FilterItemData,
    onDismissRequest: () -> Unit,
) {
    var isDropDownMenuShowing by remember { mutableStateOf(false) }
    var selectedTagsMapState by remember { mutableStateOf(emptyMap<TagEnum, Boolean>()) }

    Row(
        modifier = Modifier
            .wrapContentSize()
            .background(Color.DarkGray, shape = CircleShape)
            .border(4.dp, Color.DarkGray, shape = CircleShape)
            .padding(4.dp)
            .clickable { isDropDownMenuShowing = true },
        horizontalArrangement = Arrangement.spacedBy(4.dp),

        ) {
        Icon(
            painter = painterResource(
                id = R.drawable.ic_arrow_drop_down_48
            ),
            tint = Color.Gray,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = itemData.tagIdentifier.toResString(),
            color = Color.White,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontSize = 16.sp,
            modifier = Modifier
                .wrapContentHeight()
                .widthIn(0.dp, 100.dp)
        )
    }

    if (isDropDownMenuShowing) {
        // todo do list of selected tags from previous selected tags
        DropdownMenu(
            expanded = isDropDownMenuShowing,
            onDismissRequest = {
                isDropDownMenuShowing = false
                itemData.updateTagsInFilter(
                    selectedTagsMapState
                        .filter { it.value }
                        .map { it.key }
                )
                onDismissRequest()
            }
        ) {
            selectedTagsMapState.forEach { tagSelect ->
                Row {
                    RadioButton(
                        selected = tagSelect.value,
                        onClick = {
                            selectedTagsMapState = selectedTagsMapState
                                .toMutableMap().apply {
                                    put(tagSelect.key, !tagSelect.value)
                                }
                        }
                    )

                    Text(tagSelect.key.toResString())
                }
            }

            LaunchedEffect(Unit) {
                selectedTagsMapState = selectedTagsMapState
                    .toMutableMap()
                    .apply {
                        clear()

                        val tagsInFilter = itemData.getTagsInFilter()
                        itemData.tags
                            .map { it to tagsInFilter.contains(it) }
                            .forEach {
                                put(it.first, it.second)
                            }
                    }
            }
        }
    }
}

private fun constructFilterItem(
    viewModel: FilterGridViewModel,
    tagIdentifier: TagIdentifierEnum,
    tags: List<TagEnum>
) = FilterItemData(
    tagIdentifier = tagIdentifier,
    tags = tags,
    getTagsInFilter = {
        viewModel.state.value.filters[tagIdentifier] ?: listOf()
    },
    updateTagsInFilter = { tagEnumList ->
        viewModel.onEvent(
            FilterGridViewModel.Event.UpdateFilter(
                tagIdentifier to tagEnumList
            )
        )
    }
)


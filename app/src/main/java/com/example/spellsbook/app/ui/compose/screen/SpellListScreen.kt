package com.example.spellsbook.app.ui.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spellsbook.R
import com.example.spellsbook.app.mapper.toResString
import com.example.spellsbook.app.ui.compose.item.SpellsListItem
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.domain.enums.CastingTimeEnum
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.enums.RangeEnum
import com.example.spellsbook.domain.enums.RitualEnum
import com.example.spellsbook.domain.enums.SchoolEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.SourceEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.usecase.GetSpellsWithFilterAndSorterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SpellsViewModel @Inject constructor(
    private val getSpellsWithFilterAndSorterUseCase: GetSpellsWithFilterAndSorterUseCase,
//    private val // todo addSpellToBookUseCase
) : ViewModel() {
    data class State(
        val filters: Map<TagIdentifierEnum, List<TagEnum>> = emptyMap(),
        val sorter: SortOptionEnum = SortOptionEnum.BY_NAME,
        val spells: List<SpellShortModel> = emptyList(),
    )

    sealed class Event {
        class UpdateFilter(val pair: Pair<TagIdentifierEnum, List<TagEnum>>) : Event()
        class ChangeSorter(val sorter: SortOptionEnum) : Event()
        object ClearFilterAndSorter : Event()
    }

    private val _state = MutableStateFlow(State())
    val state = _state.map { state ->
        state.copy(
            spells = getSpellsWithFilterAndSorterUseCase
                .execute(state.filters, state.sorter)
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), State())

    fun onEvent(event: Event) {
        when (event) {
            is Event.UpdateFilter -> {
                println(event.pair)
                _state.value = _state.value.copy(
                    filters = _state.value.filters + event.pair
                )
            }

            is Event.ChangeSorter -> {
                _state.value = _state.value.copy(
                    sorter = event.sorter
                )
            }

            is Event.ClearFilterAndSorter -> {
                _state.value = State()
            }
        }
    }

    fun getFilters() = _state.value.filters
}

@Composable
fun SpellsScreen(
    navController: NavController,
    bookId: Long? = null,
    viewModel: SpellsViewModel = hiltViewModel()
) {
    val wrapContentModifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.White
            ),
    ) {
        SearchRow()
        SortRow()
        FilterGrid(
            viewModel = viewModel,
            modifier = wrapContentModifier,
        )
        SpellsList(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
private fun SearchRow(
    modifier: Modifier = Modifier
) {

}

class TagSelect(
    val tag: TagEnum,
    var isSelected: Boolean
)

data class FilterItemData(
    val tagIdentifier: TagIdentifierEnum,
    val tags: List<TagEnum>,
    val getTagsInFilter: () -> List<TagEnum>,
    val updateTagsInFilter: (List<TagEnum>) -> Unit
)

@Composable
fun FilterGrid(
    modifier: Modifier = Modifier,
    viewModel: SpellsViewModel
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
        LazyVerticalStaggeredGrid(
            modifier = modifier,
            columns = StaggeredGridCells.Fixed(3),
            contentPadding = PaddingValues(4.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            items(filterItemDataList) {
                FilterItem(
                    it.component1(),
                    it.component2(),
                    it.component3(),
                    it.component4()
                )
            }
        }
    }
}

private fun constructFilterItem(
    viewModel: SpellsViewModel,
    tagIdentifier: TagIdentifierEnum,
    tags: List<TagEnum>
) = FilterItemData(
    tagIdentifier = tagIdentifier,
    tags = tags,
    getTagsInFilter = {
        viewModel.getFilters()[tagIdentifier] ?: listOf()
    },
    updateTagsInFilter = { tagEnumList ->
        viewModel.onEvent(
            SpellsViewModel.Event.UpdateFilter(
                tagIdentifier to tagEnumList
            )
        )
    }
)

@Composable
fun FilterItem(
    tagIdentifier: TagIdentifierEnum,
    tags: List<TagEnum>,
    getTagsInFilter: () -> List<TagEnum>,
    updateTagsInFilter: (List<TagEnum>) -> Unit
) {
    var isDropDownMenuShowing by remember { mutableStateOf(false) }
    val selectedTagsMap = remember { mutableStateMapOf<TagEnum, Boolean>() } // todo rewrite

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
            text = tagIdentifier.toResString(),
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
                updateTagsInFilter(
                    selectedTagsMap
                        .filter { it.value }
                        .map { it.key }
                )
            }
        ) {
            selectedTagsMap.forEach { tagSelect ->
                Row {
                    RadioButton(
                        selected = tagSelect.value,
                        onClick = {
                            selectedTagsMap[tagSelect.key] = !tagSelect.value
                        }
                    )

                    Text(tagSelect.key.toResString())
                }
            }
        }

        SideEffect {
            selectedTagsMap.clear()
            val tagsInFilter = getTagsInFilter()
            selectedTagsMap.putAll(
                tags.map { it to tagsInFilter.contains(it) }
            )
            println(selectedTagsMap)
        }
    }
}

@Composable
private fun SortRow() {
}

@Composable
fun SpellsList(
    navController: NavController,
    viewModel: SpellsViewModel,
    bookId: Long? = null
) {
    val state = viewModel.state.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(state.value.spells) { spell ->
            SpellsListItem(
                navigate = {
                    navController.navigate(
                        NavEndpoint
                            .SpellByUuid
                            .getDestination(spell.uuid)
                    )
                },
                spell = spell,
                bookId = bookId
            )
        }
    }
}


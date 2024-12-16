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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.spellsbook.R
import com.example.spellsbook.app.mapper.toResourceString
import com.example.spellsbook.app.ui.compose.navigation.NavEndpoint
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.enums.SchoolEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
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

class TagInfo(
    val tag: TagEnum,
    val showingName: String,
    var isSelected: Boolean
)

class FilterItemData(
    val name: String,
    val tagInfoList: List<TagInfo>,
    val updateFilter: (List<TagEnum>) -> Unit
)

@Composable
fun FilterGrid(
    modifier: Modifier = Modifier,
    viewModel: SpellsViewModel
) {
    val filterItemDataList = listOf(
        FilterItemData(
            name = stringResource(id = R.string.tag_level_name),
            tagInfoList = LevelEnum.entries.map {
                TagInfo(
                    it, it.toResourceString(),
                    viewModel.state.value.filters[TagIdentifierEnum.LEVEL]
                        ?.contains(it) ?: false
                )
            },
            updateFilter = {
                viewModel.onEvent(
                    SpellsViewModel.Event.UpdateFilter(TagIdentifierEnum.LEVEL to it)
                )
            }
        ),
        FilterItemData(
            name = stringResource(id = R.string.tag_school_name),
            tagInfoList = SchoolEnum.entries.map {
                TagInfo(
                    it, it.toResourceString(),
                    viewModel.state.value.filters[TagIdentifierEnum.SCHOOL]
                        ?.contains(it) ?: false
                )
            },
            updateFilter = {
                viewModel.onEvent(
                    SpellsViewModel.Event.UpdateFilter(TagIdentifierEnum.SCHOOL to it)
                )
            }
        ),
    )

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
                    it.name,
                    it.tagInfoList,
                    it.updateFilter
                )
            }
        }

//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceEvenly
//        ) {
//            Button(
//                modifier = Modifier
//                    .wrapContentSize(),
//                onClick = {
//                    viewModel.applyFilter()
//                }
//            ) {
//                Text(stringResource(id = R.string.apply_filters))
//            }
//        }
    }
}

@Composable
fun FilterItem(
    name: String,
    tagInfoList: List<TagInfo>,
    updateFilter: (List<TagEnum>) -> Unit
) {
    var isDropDownMenuShowing by remember { mutableStateOf(false) }

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
            text = name,
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
        DropdownMenu(
            expanded = isDropDownMenuShowing,
            onDismissRequest = {
                isDropDownMenuShowing = false
                updateFilter(
                    tagInfoList
                        .filter { it.isSelected }
                        .map { it.tag }
                )
            }
        ) {
            tagInfoList.forEach { tagInfo ->
//                var selected by remember { mutableStateOf(tagInfo.isSelected) }
                Row {
                    RadioButton(
                        selected = tagInfo.isSelected,
                        onClick = {
                            tagInfo.isSelected = !tagInfo.isSelected
                        }
                    )

                    Text(tagInfo.showingName)
                }
            }
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
                navigate = { navController.navigate(NavEndpoint.Spells.route + "/${spell.uuid}") },
                spell = spell,
                bookId = bookId
            )
        }
    }
}

@Composable
fun SpellsListItem(
    navigate: () -> Unit,
    spell: SpellShortModel,
    bookId: Long? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable(onClick = navigate),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = spell.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = spell.level?.toResourceString() ?: "N/A",

                )
        }

        if (bookId != null)
            Button(
                modifier = Modifier.size(24.dp),
                onClick = {

                }
            ) {

            }
    }
}
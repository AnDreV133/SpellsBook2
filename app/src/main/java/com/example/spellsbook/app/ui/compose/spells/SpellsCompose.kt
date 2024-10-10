package com.example.spellsbook.app.ui.compose.spells

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.spellsbook.R
import com.example.spellsbook.app.mapper.toResourceString
import com.example.spellsbook.app.ui.compose.emitState
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.enums.SchoolEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.usecase.GetSpellsWithFilterAndSorterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TagCollectionForFilter(
    val levels: List<TagEnum> = emptyList(),
    val schools: List<TagEnum> = emptyList(),
) {
    fun toList(): List<List<TagEnum>> =
        listOf(levels, schools)
}

sealed class ChangeTag(val values: List<TagEnum>) {
    class Level(values: List<TagEnum>) : ChangeTag(values)
    class School(values: List<TagEnum>) : ChangeTag(values)
}

@HiltViewModel
class SpellsViewModel @Inject constructor(
    private val getSpellsWithFilterAndSorterUseCase: GetSpellsWithFilterAndSorterUseCase,
//    private val // todo addSpellToBookUseCase
) : ViewModel() {
    data class State(
        val tags: TagCollectionForFilter = TagCollectionForFilter(),
        val spells: List<SpellShortModel> = emptyList(),
    )

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    init {
        applyFilter()
    }

    fun addSpellToBook(spellUuid: String, bookId: Long) {

    }

    fun removeSpellFromBook(spellUuid: String, bookId: Long) {

    }

    fun updateFilter(filters: ChangeTag) {
        emitState(_state) {
            state.value.run {
                copy(
                    tags = when (filters) {
                        is ChangeTag.Level -> tags.copy(
                            levels = filters.values
                        )

                        is ChangeTag.School -> tags.copy(
                            schools = filters.values
                        )

                        else -> throw IllegalArgumentException("Unknown filter type")
                    }
                )
            }
        }
    }

    fun applyFilter() {
//        emitState(_state) {
////            println(
////                state.value.tags.toList()
////            )
//            val temp = getSpellsWithFilterAndSorterUseCase.execute(
//                filter = state.value.tags.toList()
//            )
//
//            println(temp)
//            println(temp.size)
//
//            state.value.copy(
//                spells = temp
//            )
//        }
    }

//    fun clearFilter() {
//        emitState(_state) {
//            State(
//                spells = getSpellsWithFilterAndSorterUseCase.execute()
//            )
//        }
//    }
}

class SpellsCompose(
    val navController: NavHostController,
    val bookId: Long? = null // if bookId is null, screen without add button on spells
) {

    class FilterItemData(
        val name: String,
        val changedFilterItems: MutableMap<TagEnum, Boolean>,
        val updateFilter: (List<TagEnum>) -> Unit,
        val tagToResString: @Composable (TagEnum) -> String,
    )

    @Composable
    fun Screen(
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
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize()
            )
        }
    }

    @Composable
    private fun SearchRow(
        modifier: Modifier = Modifier
    ) {

    }

    @Composable
    fun FilterGrid(
        modifier: Modifier = Modifier,
        viewModel: SpellsViewModel
    ) {
        val items = listOf(
            FilterItemData(
                name = stringResource(id = R.string.tag_level_name),
                changedFilterItems = mutableMapOf<TagEnum, Boolean>().apply {
                    LevelEnum.entries.forEach { put(it, it in viewModel.state.value.tags.levels) }
                },
                updateFilter = { filter -> viewModel.updateFilter(ChangeTag.Level(filter)) },
                tagToResString = { tag -> LevelEnum.valueOf(tag.toString()).toResourceString() },
            ),
            FilterItemData(
                name = stringResource(id = R.string.tag_school_name),
                changedFilterItems = mutableMapOf<TagEnum, Boolean>().apply {
                    SchoolEnum.entries.forEach { put(it, it in viewModel.state.value.tags.schools) }
                },
                updateFilter = { filter -> viewModel.updateFilter(ChangeTag.School(filter)) },
                tagToResString = { tag -> SchoolEnum.valueOf(tag.toString()).toResourceString() },
            )
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
                items(items) {
                    FilterItem(it)
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    modifier = Modifier
                        .wrapContentSize(),
                    onClick = {
                        viewModel.applyFilter()
                    }
                ) {
                    Text(stringResource(id = R.string.apply_filters))
                }
            }
        }
    }

    @Composable
    fun FilterItem(
        filterItemData: FilterItemData
    ) {
        var showDropDownMenu by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()

        Row(
            modifier = Modifier
                .wrapContentSize()
                .background(Color.DarkGray, shape = CircleShape)
                .border(4.dp, Color.DarkGray, shape = CircleShape)
                .padding(4.dp)
                .clickable { showDropDownMenu = true },
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
                text = filterItemData.name,
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontSize = 16.sp,
                modifier = Modifier
                    .wrapContentHeight()
                    .widthIn(0.dp, 100.dp)
            )
        }

        if (showDropDownMenu) {
            DropdownMenu(
                expanded = showDropDownMenu,
                onDismissRequest = { showDropDownMenu = false }
            ) {
                filterItemData.changedFilterItems.forEach { filter ->
                    var selected by remember { mutableStateOf(filter.value) }
                    Row {
                        RadioButton(
                            selected = selected,
                            onClick = {
                                selected = !selected

                                coroutineScope.launch {
                                    filterItemData.apply {
                                        changedFilterItems.replace(filter.key, selected)
                                        updateFilter(changedFilterItems
                                            .filter { it.value }
                                            .map { it.key }
                                        )
                                    }
                                }

                            }
                        )

                        Text(
                            filterItemData.tagToResString(filter.key)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun SortRow(
        modifier: Modifier = Modifier
    ) {
    }

    @Composable
    fun SpellsList(
        viewModel: SpellsViewModel,
        modifier: Modifier = Modifier,
    ) {
        val state = viewModel.state.collectAsState()
        println("-> ${state.value}")
        LazyColumn(
            modifier = modifier
        ) {
            items(state.value.spells) { spell ->
                SpellsListItem(spell = spell)
            }
        }
    }

    @Composable
    fun SpellsListItem(
        spell: SpellShortModel,
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
                    .clickable {
                        // todo open spell screen
                    },
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = spell.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = spell.level.toString(),

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
}


@Preview
@Composable
fun SpellsPreview_() {

}
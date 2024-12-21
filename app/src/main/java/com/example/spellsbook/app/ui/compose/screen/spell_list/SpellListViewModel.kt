package com.example.spellsbook.app.ui.compose.screen.spell_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.usecase.AddSpellToBookUseCase
import com.example.spellsbook.domain.usecase.GetSpellsWithFilterAndSorterByBookIdUseCase
import com.example.spellsbook.domain.usecase.GetSpellsWithFilterAndSorterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SpellListViewModel_ @Inject constructor(
    private val getSpellsWithFilterAndSorterUseCase: GetSpellsWithFilterAndSorterUseCase,
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


class SpellListWithAddToBookViewModel @Inject constructor(
    private val getSpellsWithFilterAndSorterByBookIdUseCase: GetSpellsWithFilterAndSorterByBookIdUseCase,
    private val addSpellToBookUseCase: AddSpellToBookUseCase,
) {

}
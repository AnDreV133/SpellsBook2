package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.repository.SpellByAuthorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSpellsShortByAuthorUseCase @Inject constructor(
    val spellByAuthorRepository: SpellByAuthorRepository
) {
    fun execute(): Flow<List<SpellShortModel>> {
        return spellByAuthorRepository.getAllSpellsShort()
    }
}
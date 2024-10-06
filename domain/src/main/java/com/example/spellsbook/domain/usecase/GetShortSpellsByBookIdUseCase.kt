package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.repository.SpellRepository
import javax.inject.Inject

class GetShortSpellsByBookIdUseCase @Inject constructor(
    private val spellRepository: SpellRepository,
    private val locale: LocaleEnum
) {
    suspend fun execute(id: Long): List<SpellShortModel> {
        return spellRepository.getSpellsByBookId(id, locale)
    }
}
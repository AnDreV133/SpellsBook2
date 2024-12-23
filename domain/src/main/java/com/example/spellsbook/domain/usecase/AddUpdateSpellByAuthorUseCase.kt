package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.model.SpellTagsModel
import com.example.spellsbook.domain.repository.SpellByAuthorRepository
import javax.inject.Inject

class AddUpdateSpellByAuthorUseCase @Inject constructor(
    val spellByAuthorRepository: SpellByAuthorRepository
) {
    suspend fun execute(tags: SpellTagsModel, spell: SpellDetailModel) {
        spellByAuthorRepository.addUpdateSpell(tags, spell)
    }
}
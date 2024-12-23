package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.enums.SourceEnum
import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.model.SpellTagsModel
import com.example.spellsbook.domain.repository.SpellByAuthorRepository
import java.util.UUID
import javax.inject.Inject

class AddUpdateSpellByAuthorUseCase @Inject constructor(
    val spellByAuthorRepository: SpellByAuthorRepository
) {
    suspend fun execute(tags: SpellTagsModel, spell: SpellDetailModel) {
        spellByAuthorRepository.addUpdateSpell(tags, spell)
    }

    suspend fun executeNew(baseName: String) {
        execute(
            SpellTagsModel(
                source = SourceEnum.BY_AUTHOR,
            ),
            SpellDetailModel(
                name = baseName,
                uuid = UUID.randomUUID().toString(),
            )
        )
    }
}
package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.enums.LocaleEnum
import com.example.spellsbook.domain.enums.SourceEnum
import com.example.spellsbook.domain.enums.toDigit
import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.model.SpellTagsModel
import com.example.spellsbook.domain.model.SpellTemplateModel
import com.example.spellsbook.domain.repository.SpellByAuthorRepository
import java.util.UUID
import javax.inject.Inject

class AddUpdateSpellByAuthorUseCase @Inject constructor(
    private val spellByAuthorRepository: SpellByAuthorRepository,
    private val language: LocaleEnum
) {
    suspend fun execute(
        tags: SpellTagsModel,
        spell: SpellDetailModel
    ) {
//        val temp = SpellDetailModel(
//            name = spell.name,
//            uuid = uuid,
//            level = tags.level?.toDigit()?.toString() ?: "",
//            source = when (language) {
//                LocaleEnum.RUSSIAN -> "Авторское"
//                else -> "Author"
//            },
//
//            )
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
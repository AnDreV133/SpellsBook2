package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.enums.LocaleEnum
import com.example.spellsbook.domain.repository.SpellRepository
import javax.inject.Inject

class GetSpellDetailUseCase @Inject constructor(
    private val spellRepository: SpellRepository,
    private val locale: LocaleEnum
) {
    suspend fun execute(uuid: String) =
        spellRepository.getSpellDetailByUuid(uuid, locale)

    suspend fun executeWithTags(uuid: String) =
        spellRepository.getSpellWithTagsDetailByUuid(uuid, locale)

    suspend fun executeWithTagsNonLocale(uuid: String) =
        spellRepository.getSpellWithTagsDetailByUuid(uuid, LocaleEnum.DEFAULT)
}
package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.repository.SpellRepository
import javax.inject.Inject

class GetSpellDetailUseCase @Inject constructor(
    private val spellRepository: SpellRepository,
    private val locale: LocaleEnum
) {
    suspend fun execute(uuid: String) =
        spellRepository.getSpellDetailByUuid(uuid, locale)
}
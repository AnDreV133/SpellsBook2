package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.repository.SpellByAuthorRepository
import javax.inject.Inject

class GetSpellDetailByAuthorUseCase @Inject constructor(
    val spellByAuthorRepository: SpellByAuthorRepository
) {
    suspend fun execute(uuid: String): SpellDetailModel {
        return spellByAuthorRepository.getSpellDetail(uuid)
    }
}
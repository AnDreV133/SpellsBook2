package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.repository.SpellByAuthorRepository
import javax.inject.Inject

class RemoveSpellByAuthorUseCase @Inject constructor(
    private val spellByAuthorRepository: SpellByAuthorRepository
) {
    suspend fun execute(uuid: String) {
        spellByAuthorRepository.removeSpell(uuid)
    }
}
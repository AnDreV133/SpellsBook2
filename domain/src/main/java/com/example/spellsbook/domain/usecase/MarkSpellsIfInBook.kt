package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.repository.BooksWithSpellsRepository
import javax.inject.Inject

class MarkSpellsIfInBook @Inject constructor(
    private val booksWithSpellsRepository: BooksWithSpellsRepository,
) {
    suspend fun execute(
        bookId: Long,
        spells: List<SpellShortModel>,
    ): List<Pair<SpellShortModel, Boolean>> {
        val uuidInBook = booksWithSpellsRepository.getUuidsByBookId(bookId)

        return spells
            .map { spell ->
                spell to (spell.uuid in uuidInBook)
            }
    }
}
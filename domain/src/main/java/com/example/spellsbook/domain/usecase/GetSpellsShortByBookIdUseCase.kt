package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.repository.BooksWithSpellsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSpellsShortByBookIdUseCase @Inject constructor(
    private val booksWithSpellsRepository: BooksWithSpellsRepository,
    private val locale: LocaleEnum
) {
    fun execute(bookId: Long): Flow<List<SpellShortModel>> {
        return booksWithSpellsRepository.getSpellsByBookId(bookId, locale.value)
    }
}
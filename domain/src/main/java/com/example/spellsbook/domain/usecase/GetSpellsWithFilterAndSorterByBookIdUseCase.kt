package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.repository.BooksWithSpellsRepository
import com.example.spellsbook.domain.repository.SpellRepository
import javax.inject.Inject

class GetSpellsWithFilterAndSorterByBookIdUseCase @Inject constructor(
    private val spellRepository: SpellRepository,
    private val booksWithSpellsRepository: BooksWithSpellsRepository,
    private val locale: LocaleEnum
) {
    suspend fun execute(
        bookId: Long,
        filter: Map<TagIdentifierEnum, List<TagEnum>> = emptyMap(),
        sorter: SortOptionEnum = SortOptionEnum.BY_NAME,
    ): List<Pair<SpellShortModel, Boolean>> {
        val uuidInBook = booksWithSpellsRepository.getUuidsByBookId(bookId).toMutableSet()

        var prevUuidInBookSize = uuidInBook.size
        return spellRepository.getSpellsShort(filter, sorter, locale).map {
            uuidInBook -= it.uuid
            val inBook = uuidInBook.size != prevUuidInBookSize
            prevUuidInBookSize = uuidInBook.size
            Pair(
                it,
                inBook
            )
        }
    }
}
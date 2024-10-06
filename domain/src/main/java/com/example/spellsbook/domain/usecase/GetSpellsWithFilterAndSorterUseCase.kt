package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.repository.SpellRepository
import com.example.spellsbook.domain.repository.TagRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetSpellsWithFilterAndSorterUseCase @Inject constructor(
    private val spellRepository: SpellRepository,
    private val tagRepository: TagRepository,
    private val locale: LocaleEnum
) {
//    var pageSize = 20
//        set(value) {
//            field =
//                if (value > 0) value
//                else throw IllegalArgumentException("pageSize must be greater than 0")
//        }

    suspend fun execute(
        filter: List<List<TagEnum>> = emptyList(),
        sorter: List<Any> = emptyList()
    ): List<SpellShortModel> = withContext(Dispatchers.IO) {
        return@withContext if (filter.isEmpty() || filter.all { it.isEmpty() })
            spellRepository.gatAllSpells(locale)
        else {
            var resultUuids = setOf<String>()
            filter.map { category ->
                category.map { tag ->
                    async {
                        tagRepository.getUuids(tag)
                    }
                }
                    .awaitAll()
                    .flatten()
                    .toSet()
            }.forEach { uuids ->
                if (uuids.isEmpty()) return@forEach

                resultUuids =
                    if (resultUuids.isEmpty()) uuids
                    else resultUuids.intersect(uuids)
            }

            resultUuids.map {
                async { spellRepository.getSpellsByUuid(it, locale = locale) }
            }.awaitAll().flatten()
        }
    }
}
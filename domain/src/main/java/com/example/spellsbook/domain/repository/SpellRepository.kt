package com.example.spellsbook.domain.repository

import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.model.SpellShortModel

interface SpellRepository {
    suspend fun getSpellsByBookId(bookId: Long, locale: LocaleEnum): List<SpellShortModel>
    suspend fun gatAllSpells(locale: LocaleEnum): List<SpellShortModel>
    suspend fun getSpellsByUuid(vararg uuids: String, locale: LocaleEnum): List<SpellShortModel>
}
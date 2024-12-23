package com.example.spellsbook.domain.repository

import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.model.SpellTagsModel
import kotlinx.coroutines.flow.Flow

interface SpellByAuthorRepository {
    suspend fun removeSpell(uuid: String)

    suspend fun addUpdateSpell(tags: SpellTagsModel, spell: SpellDetailModel)

    fun getAllSpellsShort(): Flow<List<SpellShortModel>>

    suspend fun getSpellDetail(uuid: String): SpellDetailModel
}
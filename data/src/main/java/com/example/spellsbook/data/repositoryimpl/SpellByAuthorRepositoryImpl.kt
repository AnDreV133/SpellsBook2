package com.example.spellsbook.data.repositoryimpl

import com.example.spellsbook.data.mapper.mapToDetailModel
import com.example.spellsbook.data.mapper.mapToJson
import com.example.spellsbook.data.mapper.mapToShortModel
import com.example.spellsbook.data.store.dao.SpellByAuthorDao
import com.example.spellsbook.data.store.dao.SpellDao
import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import com.example.spellsbook.domain.enums.LocaleEnum
import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.model.SpellTagsModel
import com.example.spellsbook.domain.repository.SpellByAuthorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SpellByAuthorRepositoryImpl (
    private val spellByAuthorDao: SpellByAuthorDao,
    private val spellDao: SpellDao
) : SpellByAuthorRepository{
    override suspend fun removeSpell(uuid: String) {
        spellByAuthorDao.remove(uuid)
    }

    override suspend fun addUpdateSpell(tags: SpellTagsModel, spell: SpellDetailModel) {
        spellByAuthorDao.insert(
            TaggingSpellEntity(
                uuid = spell.uuid,
                levelTag = tags.level?.toString(),
                schoolTag = tags.school?.toString(),
                castingTime = tags.castingTime?.toString(),
                rangeTag = tags.range?.toString(),
                ritualTag = tags.ritual?.toString(),
                sourceTag = tags.source?.toString()
            ),
            SpellEntity(
                uuid = spell.uuid,
                name = spell.name,
                json = spell.mapToJson().toString()
            )
        )
    }

    override fun getAllSpellsShort(): Flow<List<SpellShortModel>> =
        spellByAuthorDao.getAllShort()
            .map { list ->
                list.map {
                    it.mapToShortModel()
                }
            }

    override suspend fun getSpellDetail(uuid: String): SpellDetailModel =
        spellDao.getSpellDetail(uuid, LocaleEnum.DEFAULT.value)
            .mapToDetailModel()
}
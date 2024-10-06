package com.example.spellsbook.data.repositoryimpl

import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.enums.SchoolEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.data.store.dao.LevelDao
import com.example.spellsbook.data.store.dao.SchoolDao
import com.example.spellsbook.domain.repository.TagRepository

class TagRepositoryImpl(
    private val levelDao: LevelDao,
    private val schoolDao: SchoolDao
) : TagRepository {
    override suspend fun getUuids(tag: TagEnum) =
        when (tag) {
            is LevelEnum -> levelDao.getByTag(tag)
            is SchoolEnum -> schoolDao.getByTag(tag)
            else -> emptyList()
        }.map { it.spellUuid }
}

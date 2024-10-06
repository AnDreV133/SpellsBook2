package com.example.spellsbook.data.store.dao

import androidx.room.Dao
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.data.store.entity.tags.BaseTagEntity
import com.example.spellsbook.data.store.entity.tags.LevelEntity
import com.example.spellsbook.data.store.entity.tags.SchoolEntity

abstract class TagDao<T : BaseTagEntity>(
    private val tableName: String
) : BaseDao<T>(tableName) {
    suspend fun getByTag(tag: TagEnum) = _getMany(
        SimpleSQLiteQuery(
            "select * from $tableName " +
                    "where ${BaseTagEntity.COLUMN_TAG} = '$tag'"
        )
    )
}

@Dao
abstract class LevelDao : TagDao<LevelEntity>(LevelEntity.TABLE_NAME)

@Dao
abstract class SchoolDao : TagDao<SchoolEntity>(SchoolEntity.TABLE_NAME)
package com.example.spellsbook.data.store.dao

import androidx.room.Dao
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.spellsbook.data.store.entity.spells.SpellEntity
import com.example.spellsbook.data.store.entity.spells.SpellEntityEn
import com.example.spellsbook.data.store.entity.spells.SpellEntityRu

abstract class SpellDao<T : SpellEntity>(
    private val tableName: String
) : BaseDao<T>(tableName) {
    suspend fun getByUuid(uuid: List<String>): List<SpellEntity> =
        _getMany(
            SimpleSQLiteQuery(
                "select * from $tableName " +
                        "where ${SpellEntity.COLUMN_UUID} " +
                        "in (${uuid.joinToString { "'$it'" }})"
            )
        )

    suspend fun getByUuid(uuid: String): SpellEntity =
        getByUuid(listOf(uuid)).let { it[0] }
}

@Dao
abstract class SpellDaoEn : SpellDao<SpellEntityEn>(SpellEntityEn.TABLE_NAME)

@Dao
abstract class SpellDaoRu : SpellDao<SpellEntityRu>(SpellEntityRu.TABLE_NAME)

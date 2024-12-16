package com.example.spellsbook.data.store.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.TaggingSpellEntity

@Dao
abstract class InitDao {
    @Transaction
    open suspend fun clearAndInsertTaggingSpells(taggingSpellEntity: List<TaggingSpellEntity>) {
        deleteAllTaggingSpells()
        insertTaggingSpells(taggingSpellEntity)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertTaggingSpells(taggingSpellEntity: List<TaggingSpellEntity>)

    @Query("delete from ${TaggingSpellEntity.TABLE_NAME}")
    protected abstract suspend fun deleteAllTaggingSpells()

    @Transaction
    open suspend fun clearAndInsertSpells(spellEntityList: List<SpellEntity>) {
        deleteAllSpells()
        insertSpells(spellEntityList)
    }

    @Query("delete from ${SpellEntity.TABLE_NAME}")
    protected abstract suspend fun deleteAllSpells()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertSpells(spellEntityList: List<SpellEntity>)
}

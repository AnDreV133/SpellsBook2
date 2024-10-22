package com.example.spellsbook.data.store.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction

import com.example.spellsbook.data.store.entity.SpellEntity;
import com.example.spellsbook.data.store.entity.TaggingSpellEntity

@Dao
abstract class InitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun _insertTaggingSpells(taggingSpellEntity: List<TaggingSpellEntity>)

    @Transaction
    open suspend fun insertTaggingSpells(taggingSpellEntity: List<TaggingSpellEntity>) {
        _insertTaggingSpells(taggingSpellEntity)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertSpells(spellEntityList: List<SpellEntity>)

    @Query("delete from ${SpellEntity.TABLE_NAME}")
    protected abstract suspend fun deleteAllSpells()

    @Transaction
    open suspend fun clearAndInsertSpells(spellEntityList: List<SpellEntity>) {
        deleteAllSpells()
        insertSpells(spellEntityList)
    }

    @Query("select exists(select '' from ${SpellEntity.TABLE_NAME})")
    abstract suspend fun hasSpells(): Boolean
}

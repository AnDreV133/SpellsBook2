package com.example.spellsbook.data.store.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import com.example.spellsbook.data.store.entity.model.SpellWithTagsShort
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SpellByAuthorDao {
    @Query(
        """
        delete from ${TaggingSpellEntity.TABLE_NAME} 
            where ${TaggingSpellEntity.COLUMN_UUID}=:uuid     
                and ${TaggingSpellEntity.COLUMN_SOURCE_TAG}='BY_AUTHOR'
        """
    )
    abstract suspend fun remove(uuid: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertTags(spellTags: TaggingSpellEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertSpell(spell: SpellEntity)

    @Transaction
    open suspend fun insert(spellTags: TaggingSpellEntity, spell: SpellEntity) {
        insertTags(spellTags)
        insertSpell(spell)
    }
    @Query(
        """
        select * from ${TaggingSpellEntity.TABLE_NAME} as t0
            inner join ${SpellEntity.TABLE_NAME} as t1
                on t0.${TaggingSpellEntity.COLUMN_UUID} = t1.${SpellEntity.COLUMN_UUID}
            where t0.${TaggingSpellEntity.COLUMN_SOURCE_TAG}='BY_AUTHOR' 
                and t1.${SpellEntity.COLUMN_LANGUAGE}='default'
            order by t1.${SpellEntity.COLUMN_NAME} asc
        """
    )
    abstract fun getAllShort(): Flow<List<SpellWithTagsShort>>
}
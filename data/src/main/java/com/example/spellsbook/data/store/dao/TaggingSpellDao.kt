package com.example.spellsbook.data.store.dao

import androidx.room.Dao
import com.example.spellsbook.data.store.entity.TaggingSpellEntity

@Dao
abstract class TaggingSpellDao: BaseDao<TaggingSpellEntity>(TaggingSpellEntity.TABLE_NAME)


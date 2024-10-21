package com.example.spellsbook.data.store.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

abstract class BaseDao<T>(private val tableName: String) {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(data: T): Long

    @Update
    abstract suspend fun update(data: T): Int
}
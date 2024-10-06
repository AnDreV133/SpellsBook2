package com.example.spellsbook.data.store.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

abstract class BaseDao<T>(private val tableName: String) {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(data: T): Long

    @Delete
    abstract suspend fun delete(data: T)

    @Update
    abstract suspend fun update(data: T): Int

    @RawQuery
    protected abstract suspend fun _getMany(query: SupportSQLiteQuery): List<T>

    suspend fun getAll() = _getMany(
        SimpleSQLiteQuery(
            "select * from $tableName"
        )
    )

    suspend fun getInterval(from: Int, to: Int) = _getMany(
        if (from >= to)
            throw IllegalArgumentException("'from' bigger than 'to' ($from >= $to)")
        else
            SimpleSQLiteQuery(
                "select * from $tableName limit ${to - from} offset $from"
            )
    )

    @RawQuery
    protected abstract suspend fun _getOne(query: SupportSQLiteQuery): T?
}
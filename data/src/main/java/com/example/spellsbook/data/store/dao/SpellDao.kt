package com.example.spellsbook.data.store.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.spellsbook.data.store.entity.BooksSpellsXRefEntity
import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import com.example.spellsbook.data.store.entity.model.SpellWithTagsShort
import com.example.spellsbook.data.store.util.filterSuffixQuery
import com.example.spellsbook.data.store.util.getSpellsWithTagsShortQuery
import com.example.spellsbook.domain.enums.LocaleEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SpellDao : BaseDao<SpellEntity>(SpellEntity.TABLE_NAME) {
    @Query(
        """
        select * from ${BooksSpellsXRefEntity.TABLE_NAME} as t0
            inner join ${TaggingSpellEntity.TABLE_NAME} as t1
                on t0.${BooksSpellsXRefEntity.COLUMN_BOOK_ID} = :id
                    and t0.${BooksSpellsXRefEntity.COLUMN_SPELL_UUID} = t1.${TaggingSpellEntity.COLUMN_UUID}
            inner join ${SpellEntity.TABLE_NAME} as t2 
                on t1.${SpellEntity.COLUMN_UUID}=t2.${TaggingSpellEntity.COLUMN_UUID} 
                    and t2.${SpellEntity.COLUMN_LANGUAGE}=:language
        """
    )
    abstract fun getSpellsShortByBookId(id: Long, language: String): Flow<List<SpellWithTagsShort>>

    @Query(
        "SELECT * FROM ${SpellEntity.TABLE_NAME} " +
                "WHERE ${SpellEntity.COLUMN_UUID} = :uuid " +
                "AND ${SpellEntity.COLUMN_LANGUAGE} = :language " +
                "LIMIT 1"
    )
    abstract suspend fun getSpellDetail(uuid: String, language: String): SpellEntity

    @RawQuery(observedEntities = [SpellWithTagsShort::class])
    protected abstract suspend fun getManyShort(query: SupportSQLiteQuery): List<SpellWithTagsShort>

    suspend fun getSpellsShort(
        filter: Map<TagIdentifierEnum, List<TagEnum>> = emptyMap(),
        sorter: SortOptionEnum = SortOptionEnum.BY_NAME,
        language: LocaleEnum = LocaleEnum.ENGLISH
    ): List<SpellWithTagsShort> =
        getManyShort(
            SimpleSQLiteQuery(
                getSpellsWithTagsShortQuery(language)
                        + filterSuffixQuery(filter, sorter)
            )
        )


}

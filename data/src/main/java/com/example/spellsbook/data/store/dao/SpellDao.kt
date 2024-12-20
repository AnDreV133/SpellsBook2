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
import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum

@Dao
abstract class SpellDao : BaseDao<SpellEntity>(SpellEntity.TABLE_NAME) {
    @Query(
        "SELECT * FROM ${SpellEntity.TABLE_NAME} " +
                "WHERE ${SpellEntity.COLUMN_UUID} = :uuid " +
                "AND ${SpellEntity.COLUMN_LANGUAGE} = :language " +
                "LIMIT 1"
    )
    abstract suspend fun getSpellDetail(uuid: String, language: String): SpellEntity

//    @RawQuery(observedEntities = [SpellEntity::class])
//    protected abstract fun _getOneDetail(query: SupportSQLiteQuery): Flow<SpellEntity>
//
//    fun getSpellDetail(uuid: String): Flow<SpellEntity> =
//        _getOneDetail(SimpleSQLiteQuery("select * from ${SpellEntity.TABLE_NAME} where ${SpellEntity.COLUMN_UUID}='$uuid'  limit 1"))

//    fun getSpellDetail(uuid: UUID, locale: LocaleEnum): Flow<SpellEntity> =
//        _getOneDetail(
//            SimpleSQLiteQuery(
//                "select * from ${SpellEntity.TABLE_NAME} " +
//                        "where ${SpellEntity.COLUMN_UUID}='$uuid' " +
//                        "limit 1"
//            )
//        )

    @RawQuery(observedEntities = [SpellWithTagsShort::class])
    protected abstract suspend fun _getManyShort(query: SupportSQLiteQuery): List<SpellWithTagsShort>

    suspend fun getSpellsShort(
        filter: Map<TagIdentifierEnum, List<TagEnum>> = emptyMap(),
        sorter: SortOptionEnum = SortOptionEnum.BY_NAME,
        language: LocaleEnum = LocaleEnum.ENGLISH
    ): List<SpellWithTagsShort> =
        _getManyShort(
            SimpleSQLiteQuery(
                getSpellsWithTagsShortQuery(language)
                        + filterQuerySuffix(filter, sorter)
            )
        )

    suspend fun getSpellsShortByBookId(
        bookId: Long,
        filter: Map<TagIdentifierEnum, List<TagEnum>> = emptyMap(),
        sorter: SortOptionEnum = SortOptionEnum.BY_NAME,
    ): List<SpellWithTagsShort> =
        _getManyShort(
            SimpleSQLiteQuery(
                getSpellsWithTagsShortByBookIdQuery(bookId)
                        + filterQuerySuffix(filter, sorter)
            )
        )

    private fun getSpellsWithTagsShortQuery(
        language: LocaleEnum
    ) = "select * from ${TaggingSpellEntity.TABLE_NAME} as t0 " +
            "inner join ${SpellEntity.TABLE_NAME} as t1 " +
            "on t0.${SpellEntity.COLUMN_UUID}=t1.${TaggingSpellEntity.COLUMN_UUID} " +
            "and t1.${SpellEntity.COLUMN_LANGUAGE}='${language.value}'"

    private fun getSpellsWithTagsShortByBookIdQuery(
        bookId: Long
    ) = "select * from ${BooksSpellsXRefEntity.TABLE_NAME} as t0" +
            "where t0.${BooksSpellsXRefEntity.COLUMN_BOOK_ID}=$bookId " + // fixme: incorrect query
            "inner join ${TaggingSpellEntity.TABLE_NAME} as t1 " +
            "on t0.${BooksSpellsXRefEntity.COLUMN_SPELL_UUID}=t1.${TaggingSpellEntity.COLUMN_UUID} " +
            "inner join ${SpellEntity.TABLE_NAME} as t2 " +
            "on t0.${BooksSpellsXRefEntity.COLUMN_SPELL_UUID}=t2.${SpellEntity.COLUMN_UUID} "

    private fun filterQuerySuffix(
        filter: Map<TagIdentifierEnum, List<TagEnum>>,
        sorter: SortOptionEnum
    ) = StringBuilder().apply {
        // begin condition
        append(" where 1=1 ")
        // set filters
        filter.forEach { entry ->
            if (entry.value.isNotEmpty())
                append("and ${entry.key.toColumnName()} in (${entry.value.toTableFields()}) ")
        }

        // set sorter
        when (sorter) { // fixme: incorrect sort query
            SortOptionEnum.BY_NAME -> Unit

            SortOptionEnum.BY_LEVEL ->
                append(", ${TaggingSpellEntity.COLUMN_LEVEL_TAG} asc ")

            else -> throw IllegalArgumentException("sort option not supported")
        }
        append("order by ${SpellEntity.COLUMN_NAME} asc ")
    }.toString()

    private fun TagIdentifierEnum.toColumnName() = when (this) {
        TagIdentifierEnum.LEVEL -> TaggingSpellEntity.COLUMN_LEVEL_TAG
        TagIdentifierEnum.SCHOOL -> TaggingSpellEntity.COLUMN_SCHOOL_TAG
        TagIdentifierEnum.CASTING_TIME-> TaggingSpellEntity.COLUMN_CASTING_TIME_TAG
        TagIdentifierEnum.RANGE -> TaggingSpellEntity.COLUMN_RANGE_TAG
        TagIdentifierEnum.RITUAL-> TaggingSpellEntity.COLUMN_RITUAL_TAG
        TagIdentifierEnum.SOURCE -> TaggingSpellEntity.COLUMN_SOURCE_TAG
        else -> throw IllegalArgumentException("tag name not supported")
    }

    private fun List<TagEnum>.toTableFields() =
        this.joinToString { "'$it'" }
}

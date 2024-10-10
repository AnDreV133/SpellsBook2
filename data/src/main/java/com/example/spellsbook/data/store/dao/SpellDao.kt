package com.example.spellsbook.data.store.dao

import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.spellsbook.data.store.entity.BooksSpellsXRefEntity
import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.display.SpellWithTagsShort
import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagNameEnum
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
abstract class SpellDao : BaseDao<SpellEntity>(SpellEntity.TABLE_NAME) {
    @RawQuery(observedEntities = [SpellEntity::class])
    protected abstract fun _getOneDetail(query: SupportSQLiteQuery): Flow<SpellEntity>

    fun getSpellDetail(id: Long): Flow<SpellEntity> =
        _getOneDetail(SimpleSQLiteQuery("select * from ${SpellEntity.TABLE_NAME} where ${SpellEntity.COLUMN_ID}=$id limit 1"))

    fun getSpellDetail(uuid: UUID, locale: LocaleEnum): Flow<SpellEntity> =
        _getOneDetail(
            SimpleSQLiteQuery(
                "select * from ${SpellEntity.TABLE_NAME} " +
                        "where ${SpellEntity.COLUMN_SPELL_UUID}='$uuid' " +
                        "and ${SpellEntity.COLUMN_LOCALE}='$locale' " +
                        "limit 1"
            )
        )

//    fun getRawSpells(
//        locale: String,
//        filter: Map<String, List<TagEnum>> = emptyMap(),
//        sorter: SortOptionEnum = SortOptionEnum.BY_NAME
//    ): Flow<List<TaggingSpellEntity>> =
//        _getManyRaw(filterQuery(locale, filter, sorter))
//
//    @RawQuery
//    protected abstract fun _getManyRaw(query: SupportSQLiteQuery): Flow<List<TaggingSpellEntity>>

    @RawQuery(observedEntities = [SpellWithTagsShort::class])
    protected abstract fun _getManyShort(query: SupportSQLiteQuery): Flow<List<SpellWithTagsShort>>

    fun getSpellsShort(
        locale: LocaleEnum,
        filter: Map<TagNameEnum, List<TagEnum>> = emptyMap(),
        sorter: SortOptionEnum = SortOptionEnum.BY_NAME
    ): Flow<List<SpellWithTagsShort>> =
        _getManyShort(
            SimpleSQLiteQuery(
                getSpellsWithTagsShortQuery(locale)
                        + filterQuerySuffix(filter, sorter)
            )
        )

    fun getSpellsShort(
        locale: LocaleEnum,
        bookId: Long,
        filter: Map<TagNameEnum, List<TagEnum>> = emptyMap(),
        sorter: SortOptionEnum = SortOptionEnum.BY_NAME,
    ): Flow<List<SpellWithTagsShort>> =
        _getManyShort(
            SimpleSQLiteQuery(
                getSpellsWithTagsShortByBookIdQuery(locale, bookId)
                        + filterQuerySuffix(filter, sorter)
            )
        )


    private fun getSpellsWithTagsShortQuery(
        locale: LocaleEnum,
    ) = "select * from ${SpellEntity.TABLE_NAME} as t0" +
            "inner join ${TaggingSpellEntity.TABLE_NAME} as t1" +
            "on ${SpellEntity.COLUMN_LOCALE}=${locale.value} " +
            "and t0.${SpellEntity.COLUMN_SPELL_UUID}=t1.${TaggingSpellEntity.COLUMN_UUID} "

    private fun getSpellsWithTagsShortByBookIdQuery(
        locale: LocaleEnum,
        bookId: Long
    ) = "select * from ${BooksSpellsXRefEntity.TABLE_NAME} as t0" +
            "where t0.${BooksSpellsXRefEntity.COLUMN_BOOK_ID}=$bookId " +
            "inner join ${TaggingSpellEntity.TABLE_NAME} as t1 " +
            "on t0.${BooksSpellsXRefEntity.COLUMN_SPELL_UUID}=t1.${TaggingSpellEntity.COLUMN_UUID} " +
            "inner join ${SpellEntity.TABLE_NAME} as t2 " +
            "on t0.${BooksSpellsXRefEntity.COLUMN_SPELL_UUID}=t2.${SpellEntity.COLUMN_SPELL_UUID} " +
            "and t2.${SpellEntity.COLUMN_LOCALE}=${locale.value} "

    private fun filterQuerySuffix(
        filter: Map<TagNameEnum, List<TagEnum>>,
        sorter: SortOptionEnum
    ) = StringBuilder(" ").apply {
        // begin condition
        append("where 1=1 ")
        // set filters
        filter.forEach { entry ->
            if (entry.value.isNotEmpty())
                append("and ${entry.key.defineTagName()} in (${entry.value.joinToString()}) ")
        }

        // set sorter
        when (sorter) {
            SortOptionEnum.BY_NAME -> Unit

            SortOptionEnum.BY_LEVEL ->
                append(", ${TaggingSpellEntity.COLUMN_LEVEL_TAG} asc ")

            else -> throw IllegalArgumentException("sort option not supported")
        }
        append("order by ${SpellEntity.COLUMN_NAME} asc ")
    }.toString()


    private fun TagNameEnum.defineTagName() = when (this) {
        TagNameEnum.LEVEL -> TaggingSpellEntity.COLUMN_LEVEL_TAG
        TagNameEnum.SCHOOL -> TaggingSpellEntity.COLUMN_SCHOOL_TAG
        else -> throw IllegalArgumentException("tag name not supported")
    }
}

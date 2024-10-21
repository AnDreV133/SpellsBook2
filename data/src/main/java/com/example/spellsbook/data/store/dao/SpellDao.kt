package com.example.spellsbook.data.store.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.spellsbook.data.store.entity.BooksSpellsXRefEntity
import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.SpellEntityConstant
import com.example.spellsbook.data.store.entity.SpellEntityRu
import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import com.example.spellsbook.data.store.entity.display.SpellWithTagsShort
import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum
import kotlinx.coroutines.flow.Flow

@Dao
abstract class SpellDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(data: SpellEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(data: SpellEntityRu): Long

//    @Query("select * from ${SpellEntity.TABLE_NAME} where ${SpellEntity.COLUMN_LOCALE}= :locale")
//    abstract suspend fun getAll(locale: String): List<SpellWithTagsShort>

    @RawQuery(observedEntities = [SpellEntity::class, SpellEntityRu::class])
    protected abstract fun _getOneDetail(query: SupportSQLiteQuery): Flow<SpellEntity>

//    fun getSpellDetail(id: Long): Flow<SpellEntity> =
//        _getOneDetail(SimpleSQLiteQuery("select * from ${SpellEntity.TABLE_NAME} where ${SpellEntity.COLUMN_ID}=$id limit 1"))

    fun getSpellDetail(uuid: String, locale: LocaleEnum): Flow<SpellEntity> =
        _getOneDetail(
            SimpleSQLiteQuery(
                locale.toTableName().let { table ->
                    "select * from $table " +
                            "where ${SpellEntityConstant.COLUMN_UUID}='$uuid' " +
                            "and ${SpellEntityConstant.COLUMN_LOCALE}='$locale' " +
                            "limit 1"
                }
            )
        )

    @RawQuery(observedEntities = [SpellWithTagsShort::class, SpellEntity::class, SpellEntityRu::class])
    protected abstract suspend fun _getManyShort(query: SupportSQLiteQuery): List<SpellWithTagsShort>

    suspend fun getSpellsShort(
        locale: LocaleEnum,
        filter: Map<TagIdentifierEnum, List<TagEnum>> = emptyMap(),
        sorter: SortOptionEnum = SortOptionEnum.BY_NAME
    ): List<SpellWithTagsShort> =
        _getManyShort(
            SimpleSQLiteQuery(
                getSpellsWithTagsShortQuery(locale)
                        + filterQuerySuffix(filter, sorter)
            )
        )

    suspend fun getSpellsShortByBookId(
        bookId: Long,
        locale: LocaleEnum,
        filter: Map<TagIdentifierEnum, List<TagEnum>> = emptyMap(),
        sorter: SortOptionEnum = SortOptionEnum.BY_NAME,
    ): List<SpellWithTagsShort> =
        _getManyShort(
            SimpleSQLiteQuery(
                getSpellsWithTagsShortByBookIdQuery(locale, bookId)
                        + filterQuerySuffix(filter, sorter)
            )
        )

    private fun getSpellsWithTagsShortQuery(
        locale: LocaleEnum,
    ) = locale.toTableName().let { table ->
        "select * from $table as t0 " +
                "inner join ${TaggingSpellEntity.TABLE_NAME} as t1 " +
                "on t0.${SpellEntityConstant.COLUMN_UUID}=t1.${TaggingSpellEntity.COLUMN_UUID} "
    }

//    +
//            "and t0.${SpellEntity.COLUMN_LOCALE}='${locale.value}' "

    private fun getSpellsWithTagsShortByBookIdQuery(
        locale: LocaleEnum,
        bookId: Long
    ) = locale.toTableName().let { table ->
        "select * from ${BooksSpellsXRefEntity.TABLE_NAME} as t0" +
                "where t0.${BooksSpellsXRefEntity.COLUMN_BOOK_ID}=$bookId " + // fixme: incorrect query
                "inner join ${TaggingSpellEntity.TABLE_NAME} as t1 " +
                "on t0.${BooksSpellsXRefEntity.COLUMN_SPELL_UUID}=t1.${TaggingSpellEntity.COLUMN_UUID} " +
                "inner join $table as t2 " +
                "on t0.${BooksSpellsXRefEntity.COLUMN_SPELL_UUID}=t2.${SpellEntityConstant.COLUMN_UUID} "
    }

    private fun filterQuerySuffix(
        filter: Map<TagIdentifierEnum, List<TagEnum>>,
        sorter: SortOptionEnum
    ) = StringBuilder().apply {
        // begin condition
        append("where 1=1 ")
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
        append("order by ${SpellEntityConstant.COLUMN_NAME} asc ")
    }.toString()


    private fun TagIdentifierEnum.toColumnName() = when (this) {
        TagIdentifierEnum.LEVEL -> TaggingSpellEntity.COLUMN_LEVEL_TAG
        TagIdentifierEnum.SCHOOL -> TaggingSpellEntity.COLUMN_SCHOOL_TAG
        else -> throw IllegalArgumentException("tag name not supported")
    }

    private fun List<TagEnum>.toTableFields() =
        this.joinToString { "'$it'" }

    private fun LocaleEnum.toTableName() = when (this) {
        LocaleEnum.ENGLISH -> SpellEntityConstant.TABLE_NAME_EN
        LocaleEnum.RUSSIAN -> SpellEntityConstant.TABLE_NAME_RU

    }
}

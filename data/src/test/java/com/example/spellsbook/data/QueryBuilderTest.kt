package com.example.spellsbook.data

import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import com.example.spellsbook.data.store.util.filterSuffixQuery
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum
import org.junit.Test

import org.junit.Assert.*

class QueryBuilderTest {
    @Test
    fun `empty position in filter should return empty suffix`() {
        val filter = mapOf(
            TagIdentifierEnum.LEVEL to listOf<TagEnum>()
        )

        val res = filterSuffixQuery(
            filter
        )

        val target = " where 1=1 order by ${SpellEntity.COLUMN_NAME} asc"

        assertEquals(target, res)
    }

    @Test
    fun `empty filter should return empty suffix`() {
        val filter = mapOf<TagIdentifierEnum, List<TagEnum>>()

        val res = filterSuffixQuery(
            filter
        )

        val target = " where 1=1 order by ${SpellEntity.COLUMN_NAME} asc"

        assertEquals(target, res)
    }

    @Test
    fun `filter with one tag should return correct suffix`() {
        val filter = mapOf(
            TagIdentifierEnum.LEVEL to listOf(LevelEnum.LEVEL_1)
        )

        val res = filterSuffixQuery(
            filter
        )

        val target =
            " where 1=1 and ${TaggingSpellEntity.COLUMN_LEVEL_TAG} in ('LEVEL_1') " +
                    "order by ${SpellEntity.COLUMN_NAME} asc"

        assertEquals(target, res)
    }

    @Test
    fun `filter with one tag and empty position should return correct suffix`() {
        val filter = mapOf(
            TagIdentifierEnum.LEVEL to listOf(LevelEnum.LEVEL_1),
            TagIdentifierEnum.SCHOOL to listOf()
        )

        val res = filterSuffixQuery(
            filter
        )

        val target =
            " where 1=1 and ${TaggingSpellEntity.COLUMN_LEVEL_TAG} in ('LEVEL_1') " +
                    "order by ${SpellEntity.COLUMN_NAME} asc"

        assertEquals(target, res)
    }
}
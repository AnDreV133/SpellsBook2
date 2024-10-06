package com.example.spellsbook.data.store.entity.tags

abstract class BaseTagEntity {
    abstract val id: Long
    abstract val tag: String
    abstract val spellUuid: String
    companion object {
        const val COLUMN_ID: String = "id"
        const val COLUMN_TAG: String = "tag"
        const val COLUMN_SPELL_UUID: String = "spell_uuid"
    }
}


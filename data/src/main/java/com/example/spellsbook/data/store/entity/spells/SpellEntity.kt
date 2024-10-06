package com.example.spellsbook.data.store.entity.spells


abstract class SpellEntity {
    open val id: Long = 0
    abstract val uuid: String
    abstract val name: String
    abstract val json: String

    companion object {
        const val COLUMN_ID = "id"
        const val COLUMN_UUID = "uuid"
        const val COLUMN_NAME = "name"
        const val COLUMN_JSON = "json"
    }
}
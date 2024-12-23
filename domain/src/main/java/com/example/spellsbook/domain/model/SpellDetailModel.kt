package com.example.spellsbook.domain.model

data class SpellDetailModel(
    val name: String = "",
    val uuid: String,
    val level: String = "",
    val school: String = "",
    val description: String = "",
    val components: String = "",
    val materials: String = "",
    val duration: String = "",
    val range: String = "",
    val castingTime: String = "",
    val source: String = "",
)
package com.example.spellsbook.domain.json

data class DetailSpellJson(
    val name: String,
    val level: Int,
    val school: String,
    val castingTime: String,
    val range: String,
    val components: List<String>,
    val duration: String,
    val classes: List<String>,
    val subclasses: List<String>,
    val source: String,
    val description: String,
    val descriptionOnBiggerLevel: String,

    val componentVerbal: Boolean,
    val componentSomatic: Boolean,
    val componentMaterial: List<String>,
    val concentration: Boolean,
    val ritual: Boolean,
)
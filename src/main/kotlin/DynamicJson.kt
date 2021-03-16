package com.github.shichuanyes.plugin.asoul

data class DynamicJson(
    val code: Int,
    val msg: String,
    val message: String,
    val data: Data
)

data class Data(
    val cards: ArrayList<Card>
)

data class Card(
    val desc: Desc,
    val card: String
)

data class Desc(
    val dynamic_id: Long
)
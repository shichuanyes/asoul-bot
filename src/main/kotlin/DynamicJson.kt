package com.github.shichuanyes.plugin.asoul

data class DynamicJson(
    val code: Int,
    val msg: String,
    val message: String,
    val data: DynamicData
)

data class DynamicData(
    val cards: ArrayList<Card>
)

data class Card(
    val desc: Desc,
    val card: String
)

data class Desc(
    val type: Int,
    val timestamp: Long,
    val orig_type: Int,
    val user_profile: UserProfile,
    val dynamic_id_str: String
)

data class UserProfile(
    val info: Info
)

data class Info(
    val uname: String
)
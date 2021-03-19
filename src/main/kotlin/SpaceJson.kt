package com.github.shichuanyes.plugin.asoul

data class SpaceJson(
    val code: Int,
    val msg: String,
    val message: String,
    val data: SpaceData
)

data class SpaceData(
    val name: String,
    val live_room: LiveRoom
)

data class LiveRoom(
    val roomStatus: Int,
    val liveStatus: Int,
    val url: String,
    val title: String,
    val cover: String
)
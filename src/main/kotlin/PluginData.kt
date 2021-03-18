package com.github.shichuanyes.plugin.asoul

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object PluginData : AutoSavePluginData("data") {
    val userSubscribers: MutableSet<Long> by value()
    val groupSubscribers: MutableSet<Long> by value()

    var lastDynamic: Long by value()
    val liveStatus: MutableMap<Long, Int> by value()

    val watchlist: MutableSet<Long> by value(mutableSetOf(703007996L))
}
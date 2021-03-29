package com.github.shichuanyes.plugin.asoul

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object PluginData : AutoSavePluginData("data") {
    val userSubscribers: MutableSet<Long> by value()
    val groupSubscribers: MutableSet<Long> by value()

    // TODO: 3/28/2021 Restructure PluginData
    var lastDynamic: Long by value(0L)
    val liveStatus: MutableMap<Long, Int> by value()
    val watchlist: MutableSet<Long> by value()

    var inException: Boolean by value(false)
}
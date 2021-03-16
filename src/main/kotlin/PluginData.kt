package com.github.shichuanyes.plugin.asoul

import net.mamoe.mirai.console.data.AutoSavePluginData
import net.mamoe.mirai.console.data.value

object PluginData : AutoSavePluginData("data") {
    val groupSubscribers: MutableSet<Long> by value()
    val userSubscribers: MutableSet<Long> by value()
}
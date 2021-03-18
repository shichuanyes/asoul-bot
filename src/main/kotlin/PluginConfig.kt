package com.github.shichuanyes.plugin.asoul

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object PluginConfig : AutoSavePluginConfig("config") {
    var period: Long by value(30000L)
}
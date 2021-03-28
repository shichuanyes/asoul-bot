package com.github.shichuanyes.plugin.asoul

import net.mamoe.mirai.console.data.AutoSavePluginConfig
import net.mamoe.mirai.console.data.value

object PluginConfig : AutoSavePluginConfig("config") {
    var master: Long by value(0L)
    var delay: Long by value(1000L)
    var period: Long by value(30000L)
}
package com.github.shichuanyes.plugin.asoul

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.PermissionService.Companion.cancel
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info
import java.util.*

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "com.github.shichuanyes.asoul-bot",
        name = "asoul-bot",
        version = "0.9.3"
    ) {
        author("shichuanyes")
    }
) {
    // TODO: add descriptions and usages

    // TODO: add more debug support

    private var dynamicTimer: Timer? = null
    private var liveTimer: Timer? = null

    override fun onEnable() {
        PluginData.reload()
        PluginConfig.reload()

        Asoul.register()

        AbstractPermitteeId.AnyContact.permit(Asoul.permission)

        // TODO: use separate periods and add support for changing period
        dynamicTimer = Timer("UpdateDynamic", false)
        dynamicTimer?.scheduleAtFixedRate(UpdateDynamic, PluginConfig.period, PluginConfig.period)
        liveTimer = Timer("UpdateLiveStatus", false)
        liveTimer?.scheduleAtFixedRate(UpdateLiveStatus, PluginConfig.period, PluginConfig.period)

        logger.info { "Plugin asoul-bot loaded" }
    }

    override fun onDisable() {
        dynamicTimer?.cancel()
        liveTimer?.cancel()

        AbstractPermitteeId.AnyContact.cancel(Asoul.permission, true)

        Asoul.unregister()

        logger.info { "Plugin asoul-bot unloaded" }
    }

}
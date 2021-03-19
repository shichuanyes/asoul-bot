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
        version = "0.9.1"
    ) {
        author("shichuanyes")
    }
) {
    override fun onEnable() {
        PluginData.reload()
        PluginConfig.reload()

        Asoul.register()

        AbstractPermitteeId.AnyContact.permit(Asoul.permission)

        Timer("UpdateDynamics", false).schedule(UpdateDynamic, PluginConfig.period, PluginConfig.period)
        Timer("UpdateLiveStatus", false).schedule(UpdateLiveStatus, PluginConfig.period, PluginConfig.period)

        logger.info { "Plugin asoul-bot loaded" }
    }

    override fun onDisable() {
        AbstractPermitteeId.AnyContact.cancel(Asoul.permission, true)

        Asoul.unregister()

        logger.info { "Plugin asoul-bot unloaded" }
    }

}
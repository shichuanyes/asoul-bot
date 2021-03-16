package com.github.shichuanyes.plugin.asoul

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.unregister
import net.mamoe.mirai.console.permission.AbstractPermitteeId
import net.mamoe.mirai.console.permission.PermissionService.Companion.cancel
import net.mamoe.mirai.console.permission.PermissionService.Companion.permit
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

object PluginMain : KotlinPlugin(
    JvmPluginDescription(
        id = "com.github.shichuanyes.asoul-bot",
        name = "asoul-bot",
        version = "0.1.0"
    ) {
        author("shichuanyes")

        info("""
            这是一个测试插件, 
            在这里描述插件的功能和用法等.
        """.trimIndent())

        // author 和 info 可以删除.
    }
) {
    override fun onEnable() {
        PluginData.reload()

        GetTopDynamic.register()
        AbstractPermitteeId.AnyContact.permit(GetTopDynamic.permission)
        logger.info { "Plugin asoul-bot loaded" }
    }

    override fun onDisable() {
        GetTopDynamic.unregister()
        AbstractPermitteeId.AnyContact.cancel(GetTopDynamic.permission, true)
        logger.info { "Plugin asoul-bot unloaded" }
    }
}
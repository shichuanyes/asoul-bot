package com.github.shichuanyes.plugin.asoul

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User

object Asoul : CompositeCommand(
    PluginMain, primaryName = "asoul"
) {
    @SubCommand("setPeriod")
    suspend fun CommandSender.setPeriod(period: Long) {
        PluginConfig.period = period
        sendMessage("请求间隔已被设置为$period ms")
    }

    @SubCommand("getPeriod")
    suspend fun CommandSender.getPeriod() {
        sendMessage("请求间隔现在是${PluginConfig.period} ms")
    }

    @SubCommand("subscribe")
    suspend fun CommandSender.subscribe() {
        when (subject) {
            is User -> {
                val id = (subject as User).id
                PluginData.userSubscribers.add(id)
            }
            is Group -> {
                val id = (subject as Group).id
                PluginData.groupSubscribers.add(id)
            }
        }
        sendMessage("订阅成功")
    }

    @SubCommand("unsubscribe")
    suspend fun CommandSender.unsubscribe() {
        when (subject) {
            is User -> {
                val id = (subject as User).id
                PluginData.userSubscribers.remove(id)
            }
            is Group -> {
                val id = (subject as Group).id
                PluginData.groupSubscribers.remove(id)
            }
        }
        sendMessage("取消订阅成功")
    }
}
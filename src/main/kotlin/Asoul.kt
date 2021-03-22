@file:Suppress("unused")

package com.github.shichuanyes.plugin.asoul

import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.User

object Asoul : CompositeCommand(
    PluginMain, primaryName = "asoul"
) {
    // TODO: use permission

    @SubCommand("setPeriod")
    suspend fun CommandSender.setPeriod(period: Long) {
        PluginConfig.period = period
        sendMessage("请求间隔已被设置为$period ms")
    }

    @SubCommand("getPeriod")
    suspend fun CommandSender.getPeriod() {
        sendMessage("请求间隔现在是${PluginConfig.period} ms")
    }

    @SubCommand("watchlist")
    suspend fun CommandSender.watchlist() {
        sendMessage(PluginData.watchlist.toString())
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

    @SubCommand("add")
    suspend fun CommandSender.add(UID: Long) {
        // TODO: fix add logic
        PluginData.watchlist.add(UID)
        PluginData.liveStatus[UID] = 0
        sendMessage("添加成功")
    }

    @SubCommand("remove")
    suspend fun CommandSender.remove(UID: Long) {
        PluginData.watchlist.remove(UID)
        PluginData.liveStatus.remove(UID)
        sendMessage("删除成功")
    }
}
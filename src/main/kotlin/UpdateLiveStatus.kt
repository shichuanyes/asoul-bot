package com.github.shichuanyes.plugin.asoul

import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.isContentEmpty
import java.util.*

object UpdateLiveStatus : TimerTask() {
    override fun run() {

        for (mid in PluginData.watchlist) {
            val data = RequestHandler.getLiveStatus(mid).data
            if (data.live_room.roomStatus == 1) {
                if (PluginData.liveStatus[mid] != data.live_room.liveStatus) {
                    PluginData.liveStatus[mid] = data.live_room.liveStatus

                    for (bot in Bot.instances) {
                        PluginMain.launch {
                            sendLiveStatus(bot, PluginData.userSubscribers, true, data)
                            sendLiveStatus(bot, PluginData.groupSubscribers, false, data)
                        }
                    }
                }
            }
        }
    }

    private suspend fun sendLiveStatus(bot: Bot, subscribers: MutableSet<Long>, isFriend: Boolean, data: SpaceData) {
        for (subscriber in subscribers) {
            val target = if (isFriend) bot.getFriend(subscriber) else bot.getGroup(subscriber)

            var msg: Message = PlainText("")

            when (data.live_room.liveStatus) {
                1 -> {
                    msg = PlainText("@${data.name} 开播了\n" +
                            "直播间标题：${data.live_room.title}\n" +
                            "直播间链接：${data.live_room.url}")
                }
                0 -> {
                    msg = PlainText("@${data.name} 下播了")
                }
            }

            if (!msg.isContentEmpty()) target?.sendMessage(msg)
        }
    }
}
package com.github.shichuanyes.plugin.asoul

import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.message.data.PlainText
import java.lang.IllegalStateException
import java.util.*

object UpdateLiveStatus : TimerTask() {
    override fun run() {
        for (mid in PluginData.watchlist) {
            val data = RequestHandler.getLiveStatus(mid).data

            if (data.live_room.roomStatus == 1) {
                if (PluginData.liveStatus[mid] != data.live_room.liveStatus) {
                    PluginData.liveStatus[mid] = data.live_room.liveStatus

                    val text = parseLiveStatus(data)

                    for (bot in Bot.instances) {
                        PluginMain.launch {
                            Utils.sendText(bot, PluginData.userSubscribers, true, text)
                            Utils.sendText(bot, PluginData.groupSubscribers, false, text)
                        }
                    }
                }
            }
        }
    }

    private fun parseLiveStatus(data: SpaceData): PlainText {
        return when (data.live_room.liveStatus) {
            1 -> {
                PlainText("@${data.name} 开播了\n" +
                        "直播间标题：${data.live_room.title}\n" +
                        "直播间链接：${data.live_room.url}")
            }
            0 -> {
                PlainText("@${data.name} 下播了")
            }
            else -> {
                throw IllegalStateException()
            }
        }
    }
}
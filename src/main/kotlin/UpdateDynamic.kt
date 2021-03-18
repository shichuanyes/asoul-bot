package com.github.shichuanyes.plugin.asoul

import com.google.gson.Gson
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.isContentEmpty
import java.io.File
import java.util.*

object UpdateDynamic : TimerTask() {
    override fun run() {
        val gson = Gson()

        for (uid in PluginData.watchlist) {
            val dynamicJson = RequestHandler.getTopDynamic(uid)
            val card = dynamicJson.data.cards.first()

            if (PluginData.lastDynamic < card.desc.timestamp) {
                PluginData.lastDynamic = card.desc.timestamp

                val cardJson = gson.fromJson(card.card, CardJson::class.java)
                val uname = cardJson.user.uname ?: "unknown"
                val images = mutableListOf<File>()

                if (!cardJson.item.pictures.isNullOrEmpty()) {
                    for (picture in cardJson.item.pictures) {
                        val image = RequestHandler.saveImage(picture.img_src)
                        images.add(image)
                    }
                }

                for (bot in Bot.instances) {
                    PluginMain.launch {
                        val content = cardJson.item.description ?: cardJson.item.content
                        sendDynamic(bot, PluginData.userSubscribers, true, uname, content, images)
                        sendDynamic(bot, PluginData.groupSubscribers, false, uname, content, images)
                    }
                }
            }
        }
    }

    private suspend fun sendDynamic(bot: Bot, subscribers: MutableSet<Long>, isFriend: Boolean, uname: String, content: String?, streams: MutableList<File>) {
        for (subscriber in subscribers) {
            val target = if (isFriend) bot.getFriend(subscriber) else bot.getGroup(subscriber)

            target?.sendMessage("@$uname 发布了一条置顶动态")

            var msg: Message = PlainText(content ?: "")
            for (stream in streams) {
                val img = target?.uploadImage(stream)
                if (img != null) {
                    msg += img
                }
            }

            if (!msg.isContentEmpty()) target?.sendMessage(msg)
        }
    }
}
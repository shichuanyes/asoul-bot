package com.github.shichuanyes.plugin.asoul

import com.google.gson.Gson
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.isContentEmpty
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.util.*

object UpdateDynamics : TimerTask() {
    override fun run() {
        val gson = Gson()

        val dynamicJson = RequestHandler.getTopDynamic(PluginData.UID)
        val card = dynamicJson.data.cards.first()

        if (PluginData.lastDynamic != card.desc.dynamic_id) {
            PluginData.lastDynamic = card.desc.dynamic_id

            val cardJson = gson.fromJson(card.card, CardJson::class.java)

            val images = mutableListOf<File>()

            if (!cardJson.item.pictures.isNullOrEmpty()) {
                for (picture in cardJson.item.pictures) {
                    val image = RequestHandler.saveImage(picture.img_src)
                    images.add(image)
                }
            }

            PluginMain.launch {
                if (Bot.instances.isNotEmpty()) {
                    Bot.instances.forEach { bot ->
                        val content = cardJson.item.description ?: cardJson.item.content
                        sendDynamic(bot, PluginData.userSubscribers, true, content, images)
                        sendDynamic(bot, PluginData.groupSubscribers, false, content, images)
                    }
                }
            }
        }
    }

    private suspend fun sendDynamic(bot: Bot, subscribers: MutableSet<Long>, isFriend: Boolean, content: String?, streams: MutableList<File>) {
        for (subscriber in subscribers) {
            val target = if (isFriend) bot.getFriend(subscriber) else bot.getGroup(subscriber)

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
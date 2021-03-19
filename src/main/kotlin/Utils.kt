package com.github.shichuanyes.plugin.asoul

import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.isContentEmpty
import java.io.File

object Utils {
    suspend fun sendText(bot: Bot, subscribers: MutableSet<Long>, isFriend: Boolean, text: PlainText) {
        sendTextWithImages(bot, subscribers, isFriend, text, mutableListOf())
    }

    suspend fun sendTextWithImages(bot: Bot, subscribers: MutableSet<Long>, isFriend: Boolean, text: PlainText, images: MutableList<File>) {
        for (subscriber in subscribers) {
            val target = if (isFriend) bot.getFriend(subscriber) else bot.getGroup(subscriber)
            var msg: Message = text

            for (image in images) {
                val img = target?.uploadImage(image)
                if (img != null) {
                    msg += img
                }
            }

            if (!msg.isContentEmpty()) target?.sendMessage(msg)
        }
    }
}
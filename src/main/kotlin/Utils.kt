package com.github.shichuanyes.plugin.asoul

import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.isContentEmpty
import java.io.File

object Utils {
    suspend fun broadcastTextWithImages(bot: Bot, text: PlainText, images: MutableList<File>) {
        for (subscriber in PluginData.userSubscribers) sendTextWithImages(bot.getFriend(subscriber), text, images)
        for (subscriber in PluginData.groupSubscribers) sendTextWithImages(bot.getGroup(subscriber), text, images)
    }

    private suspend fun sendTextWithImages(target: Contact?, text: PlainText, images: MutableList<File>) {
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
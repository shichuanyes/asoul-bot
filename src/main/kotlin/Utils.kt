package com.github.shichuanyes.plugin.asoul

import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.isContentEmpty
import java.io.File

object Utils {
    suspend fun broadcastTextWithImages(text: PlainText, images: MutableList<File>?) {
        for (bot in Bot.instances) {
            PluginMain.launch {
                for (subscriber in PluginData.userSubscribers) sendTextWithImages(bot.getFriend(subscriber), text, images)
                for (subscriber in PluginData.groupSubscribers) sendTextWithImages(bot.getGroup(subscriber), text, images)
            }
        }
    }

    private suspend fun sendTextWithImages(target: Contact?, text: PlainText, images: MutableList<File>?) {
        var msg: Message = text
        for (image in images ?: mutableListOf()) {
            val img = target?.uploadImage(image)
            if (img != null) {
                msg += img
            }
        }
        if (!msg.isContentEmpty()) target?.sendMessage(msg)
    }
}
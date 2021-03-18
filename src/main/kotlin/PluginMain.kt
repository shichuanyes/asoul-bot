package com.github.shichuanyes.plugin.asoul

import com.google.gson.Gson
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.isContentEmpty
import net.mamoe.mirai.utils.info
import java.io.InputStream
import java.util.*
import kotlin.concurrent.timerTask

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
        PluginConfig.reload()

        val gson = Gson()

        Timer("UpdateDynamics", false).schedule(timerTask {
            val dynamicJson = RequestHandler.getTopDynamic(PluginData.UID)
            val card = dynamicJson.data.cards.first()

            if (PluginData.lastDynamic != card.desc.dynamic_id) {
                PluginData.lastDynamic = card.desc.dynamic_id

                val cardJson = gson.fromJson(card.card, CardJson::class.java)

                val streams = mutableListOf<InputStream>()

                if (!cardJson.item.pictures.isNullOrEmpty()) {
                    for (picture in cardJson.item.pictures) {
                        val stream = RequestHandler.downloadImage(picture.img_src)
                        streams.add(stream)
                    }
                }

                PluginMain.launch {
                    if (Bot.instances.isNotEmpty()) {
                        Bot.instances.forEach { b ->
                            for (subscriber in PluginData.friendSubscribers) {
                                val desc = cardJson.item.description ?: cardJson.item.content
                                sendDynamic(b, PluginData.friendSubscribers, true, desc, streams)
                                sendDynamic(b, PluginData.groupSubscribers, false, desc, streams)
                            }
                        }
                    }
                }
            }
        }, PluginConfig.period, PluginConfig.period)

        logger.info { "Plugin asoul-bot loaded" }
    }

    override fun onDisable() {
        logger.info { "Plugin asoul-bot unloaded" }
    }

    private suspend fun sendDynamic(bot: Bot, subscribers: MutableSet<Long>, isFriend: Boolean, desc: String?, streams: MutableList<InputStream>) {
        for (subscriber in subscribers) {
            val target = if (isFriend) bot.getFriend(subscriber) else bot.getGroup(subscriber)

            var msg: Message = PlainText(desc ?: "")
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
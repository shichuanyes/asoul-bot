package com.github.shichuanyes.plugin.asoul

import com.google.gson.Gson
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.message.data.PlainText
import java.io.File
import java.util.*

object UpdateDynamic : TimerTask() {
    override fun run() {
        val gson = Gson()

        for (uid in PluginData.watchlist) {
            val data = RequestHandler.getTopDynamic(uid).data
            val card = data.cards.first()

            if (PluginData.lastDynamic < card.desc.timestamp) {
                PluginData.lastDynamic = card.desc.timestamp

                val cardJson = gson.fromJson(card.card, CardJson::class.java)
                val text = parseDynamic(data, cardJson)
                val images = parseImages(cardJson)

                for (bot in Bot.instances) {
                    PluginMain.launch {
                        Utils.broadcastTextWithImages(bot, text, images)
                    }
                }
            }
        }
    }

    private fun parseDynamic(data: DynamicData, card: CardJson): PlainText {
        return PlainText("@${data.cards.first().desc.user_profile.info.uname} 发布了一条动态：\n" +
            (card.item.description ?: card.item.content)
        )
    }

    private fun parseImages(data: CardJson): MutableList<File> {
        val result = mutableListOf<File>()
        if (!data.item.pictures.isNullOrEmpty()) {
            for (picture in data.item.pictures) {
                val image = RequestHandler.saveImage(picture.img_src)
                result.add(image)
            }
        }
        return result
    }
}
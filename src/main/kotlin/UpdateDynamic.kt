package com.github.shichuanyes.plugin.asoul

import com.google.gson.Gson
import kotlinx.coroutines.launch
import net.mamoe.mirai.Bot
import net.mamoe.mirai.message.data.PlainText
import java.io.File
import java.util.*

object UpdateDynamic : TimerTask() {
    override fun run() {
        for (uid in PluginData.watchlist) {
            try {
                val data = RequestHandler.getTopDynamic(uid).data
                val card = data.cards.first()

                if (PluginData.lastDynamic < card.desc.timestamp) {
                    PluginData.lastDynamic = card.desc.timestamp
                    val (text, images) = parseCard(card.card, card.desc.type)

                    for (bot in Bot.instances) {
                        PluginMain.launch {
                            Utils.broadcastTextWithImages(bot, text, images)
                        }
                    }
                }
            } catch (e: Exception) {
                PluginMain.logger.warning(e.toString())
            }
        }
    }

    private fun parseCard(card: String, type: Int): Pair<PlainText, MutableList<File>?> {
        val gson = Gson()

        var text: String
        var images: MutableList<File>? = null

        when (type) {
            1 -> {
                val repostJson = gson.fromJson(card, RepostDynamic::class.java)
                text = "@${repostJson.user.uname} 转发了：\n" +
                    "${repostJson.item.content}\n" +
                    "-------------------------\n"
                val (origText, origImages) = parseCard(repostJson.origin, repostJson.item.orig_type)
                text += origText
                images = origImages
            }
            2 -> {
                val pictureJson = gson.fromJson(card, PictureDynamic::class.java)
                text = "@${pictureJson.user.name} 发布了：\n" +
                    pictureJson.item.description
                images = mutableListOf()
                for (picture in pictureJson.item.pictures) {
                    images.add(RequestHandler.saveImage(picture.img_src))
                }
            }
            4 -> {
                val textJson = gson.fromJson(card, TextDynamic::class.java)
                text = "@${textJson.user.uname} 发布了：\n" +
                    textJson.item.content
            }
            8 -> {
                val videoJson = gson.fromJson(card, VideoDynamic::class.java)
                text = "@${videoJson.owner.name} 投稿了视频：\n" +
                    "${videoJson.title}\n" +
                    "简介：\n" +
                    "${videoJson.desc}\n" +
                    "\n" +
                    "链接：${videoJson.short_link}"
                images = mutableListOf(RequestHandler.saveImage(videoJson.pic))
            }
            else -> {
                text = "不支持的动态类型"
            }
        }

        return Pair(PlainText(text), images)
    }
}
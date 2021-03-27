package com.github.shichuanyes.plugin.asoul

import com.github.kittinunf.fuel.core.FuelError
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import net.mamoe.mirai.message.data.PlainText
import java.io.File
import java.lang.IllegalStateException

object Parser {
    private val gson = Gson()

    @Throws(FuelError::class, JsonSyntaxException::class, IllegalStateException::class)
    internal fun parseCard(card: String, type: Int): Pair<PlainText, MutableList<File>?> {
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

    @Throws(FuelError::class, JsonSyntaxException::class, IllegalStateException::class)
    internal fun parseLiveStatus(data: SpaceData): Pair<PlainText, MutableList<File>?> {
        val images = mutableListOf(RequestHandler.saveImage(data.live_room.cover))
        return when (data.live_room.liveStatus) {
            1 -> {
                Pair(PlainText("@${data.name} 开播了\n" +
                    "直播间标题：${data.live_room.title}\n" +
                    "直播间链接：${data.live_room.url}"), images)
            }
            0 -> {
                Pair(PlainText("@${data.name} 下播了"), images)
            }
            else -> {
                throw IllegalStateException()
            }
        }
    }
}
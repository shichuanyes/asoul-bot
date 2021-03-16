package com.github.shichuanyes.plugin.asoul

import com.google.gson.Gson
import net.mamoe.mirai.console.command.CommandSender
import net.mamoe.mirai.console.command.SimpleCommand
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource

object GetTopDynamic : SimpleCommand(
    PluginMain, primaryName = "置顶动态"
) {
    @Handler
    suspend fun CommandSender.handle() {
        val gson = Gson()

        val hostUid = 703007996L
        val response = RequestHandler.getTopDynamic(hostUid)

        val cardJson = gson.fromJson(response.data.cards.first().card, CardJson::class.java)
        sendMessage(cardJson.item.description)

        if (!cardJson.item.pictures.isNullOrEmpty()) {
            for (picture in cardJson.item.pictures) {
                val stream = RequestHandler.downloadImage(picture.img_src)
                val img = subject?.uploadImage(stream.toExternalResource())
                if (img != null) {
                    sendMessage(img)
                }
            }
        }
    }
}
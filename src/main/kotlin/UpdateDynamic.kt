package com.github.shichuanyes.plugin.asoul

import com.github.kittinunf.fuel.core.FuelError
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

object UpdateDynamic : TimerTask() {
    override fun run() {
        for (uid in PluginData.watchlist) {
            try {
                val data = RequestHandler.getTopDynamic(uid).data
                val card = data.cards.first()

                if (PluginData.lastDynamic < card.desc.timestamp) {
                    PluginData.lastDynamic = card.desc.timestamp
                    val (text, images) = Parser.parseCard(card.card, card.desc.type)
                    PluginMain.launch {
                        Utils.broadcastTextWithImages(text, images)
                    }
                }
                runBlocking {
                    delay(PluginConfig.delay)
                }
            } catch (fe: FuelError) {
                PluginMain.logger.warning(fe.toString())
            } catch (ae: APIException) {
                PluginMain.logger.warning(ae.toString())
            } catch (e: Exception) {
                PluginMain.logger.error(e.toString())
            }
        }
    }


}
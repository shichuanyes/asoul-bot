package com.github.shichuanyes.plugin.asoul

import com.github.kittinunf.fuel.core.FuelError
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import java.util.*

object UpdateDynamic : TimerTask() {
    override fun run() {
        for (uid in PluginData.watchlist) {
            runBlocking {
                delay(PluginConfig.delay)
            }
            try {
                val data = RequestHandler.getTopDynamic(uid)
                val card = data.cards.first()

                PluginMain.launch {
                    Utils.reportException(false, null)
                }

                if (PluginData.lastDynamic < card.desc.timestamp) {
                    PluginData.lastDynamic = card.desc.timestamp
                    val (text, images) = Parser.parseCard(card.card, card.desc.type)
                    PluginMain.launch {
                        Utils.broadcastTextWithImages(text, images)
                    }
                }
            } catch (fe: FuelError) {
                PluginMain.logger.warning(fe.toString())
                PluginMain.launch {
                    Utils.reportException(true, fe)
                }
            } catch (ae: APIException) {
                PluginMain.logger.warning(ae.toString())
            } catch (e: Exception) {
                PluginMain.logger.error(e.toString())
            }
        }
    }
}
package com.github.shichuanyes.plugin.asoul

import com.github.kittinunf.fuel.core.FuelError
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

object UpdateLiveStatus : TimerTask() {
    override fun run() {
        for (mid in PluginData.watchlist) {
            runBlocking {
                delay(PluginConfig.delay)
            }
            try {
                val data = RequestHandler.getLiveStatus(mid)

                if (data.live_room.roomStatus == 1) {
                    if (PluginData.liveStatus[mid] != data.live_room.liveStatus) {
                        PluginData.liveStatus[mid] = data.live_room.liveStatus
                        val (text, images) = Parser.parseLiveStatus(data)
                        PluginMain.launch {
                            Utils.broadcastTextWithImages(text, images)
                        }
                    }
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
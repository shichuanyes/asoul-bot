package com.github.shichuanyes.plugin.asoul

import com.github.kittinunf.fuel.core.FuelError
import kotlinx.coroutines.launch
import java.util.*

object UpdateLiveStatus : TimerTask() {
    override fun run() {
        for (mid in PluginData.watchlist) {
            try {
                val data = RequestHandler.getLiveStatus(mid).data

                if (data.live_room.roomStatus == 1) {
                    if (PluginData.liveStatus[mid] != data.live_room.liveStatus) {
                        PluginData.liveStatus[mid] = data.live_room.liveStatus
                        val (text, images) = Parser.parseLiveStatus(data)
                        PluginMain.launch {
                            Utils.broadcastTextWithImages(text, images)
                        }
                    }
                }
                Thread.sleep(PluginConfig.delay)
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
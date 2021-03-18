package com.github.shichuanyes.plugin.asoul

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import java.io.*

object RequestHandler {
    private val gson = Gson()
    fun getTopDynamic(hostUID: Long): DynamicJson {
        val api = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history"

        val (_, response, result) = Fuel.get("$api?host_uid=$hostUID&need_top=1").responseString()
        if (result is Result.Failure) throw result.getException()
        return gson.fromJson(response.data.decodeToString(), DynamicJson::class.java)
    }

    fun saveImage(url: String): File {
        var file: File? = null
        val (_, _, result) = Fuel
            .download(url)
            .fileDestination { _, _ ->
                val urlPaths = url.split("/")
                val dir = File(System.getProperty("user.dir") + "/data/asoul-bot/img/")
                if (!dir.exists()) dir.mkdirs()
                file = File("${dir}/${urlPaths[urlPaths.lastIndex]}")
                file!!
            }.responseString()
        if (result is Result.Failure) throw result.getException()
        return file!!
    }

    fun getLiveStatus(mid: Long): SpaceJson {
        val api = "http://api.bilibili.com/x/space/acc/info"

        val (_, response, result) = Fuel.get("$api?mid=$mid").responseString()
        if (result is Result.Failure) throw result.getException()
        return gson.fromJson(response.data.decodeToString(), SpaceJson::class.java)
    }
}
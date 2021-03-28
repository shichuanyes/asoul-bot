package com.github.shichuanyes.plugin.asoul

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.*

object RequestHandler {
    private val gson = Gson()

    @Throws(FuelError::class, JsonSyntaxException::class, APIException::class)
    fun getTopDynamic(hostUID: Long): DynamicData {
        val api = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history"

        val (_, response, result) = Fuel.get("$api?host_uid=$hostUID").responseString()
        if (result is Result.Failure) throw result.getException()
        val json: DynamicJson = gson.fromJson(response.data.decodeToString(), DynamicJson::class.java)
        if (json.code != 0) throw APIException(json.code, json.message)
        return json.data
    }

    @Throws(FuelError::class)
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

    @Throws(FuelError::class, JsonSyntaxException::class, APIException::class)
    fun getLiveStatus(mid: Long): SpaceData {
        val api = "http://api.bilibili.com/x/space/acc/info"

        val (_, response, result) = Fuel.get("$api?mid=$mid").responseString()
        if (result is Result.Failure) throw result.getException()
        val json = gson.fromJson(response.data.decodeToString(), SpaceJson::class.java)
        if (json.code != 0) throw APIException(json.code, json.message)
        return json.data
    }
}
package com.github.shichuanyes.plugin.asoul

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream

object RequestHandler {
    private val gson = Gson()
    fun getTopDynamic(hostUid: Long): DynamicJson {
        val api = "https://api.vc.bilibili.com/dynamic_svr/v1/dynamic_svr/space_history"

        val (_, response, result) = Fuel.get("$api?host_uid=$hostUid&need_top=1").responseString()
        if (result is Result.Failure) throw result.getException()
        return gson.fromJson(response.data.decodeToString(), DynamicJson::class.java)
    }

    fun downloadImage(url: String): InputStream {
        var outputStream = OutputStream.nullOutputStream()
        val (_, _, result) = Fuel
            .download(url)
            .streamDestination { response: com.github.kittinunf.fuel.core.Response, _ ->
                outputStream = ByteArrayOutputStream(response.contentLength.toInt())
                Pair(outputStream as ByteArrayOutputStream) {
                    InputStream.nullInputStream()
                }
            }.responseString()
        if (result is Result.Failure) throw result.getException()
        return ByteArrayInputStream((outputStream as ByteArrayOutputStream).toByteArray())
    }
}
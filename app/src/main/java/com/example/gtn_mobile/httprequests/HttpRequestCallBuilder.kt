package com.example.gtn_mobile.httprequests

import android.content.Context
import okhttp3.Call
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class HttpRequestCallBuilder {

    private var client = OkHttpClient()

    fun buildGetCall(url: String, headers :Map<String, String>?): Call {
        var headerBuilder = Headers.Builder()
        if (headers != null) {
            val it = headers.entries.iterator()
            while (it.hasNext()) {
                val pair = it.next()
                headerBuilder.add(pair.key, pair.value)
            }
        }

        val request = Request.Builder()
            .url(url)
            .headers(headerBuilder.build())
            .get()
            .build()

        return client.newCall(request)
    }

    fun buildPostCall(url: String, params: Map<*, *>?): Call {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val jsonObject = JSONObject()
        if (params != null) {
            val it = params.entries.iterator()
            while (it.hasNext()) {
                val pair = it.next()
                jsonObject.put(pair.key.toString(), pair.value.toString())
            }
        }

        val request = Request.Builder()
            .url(url)
            .post(jsonObject.toString().toRequestBody(mediaType))
            .build()

        return client.newCall(request)
    }
}
package cn.edu.bistu.network

import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

object Api {
    private val client = OkHttpClient()

    private const val BASE_URL = "https://suited-hardy-kodiak.ngrok-free.app/"

    fun get(url: String, callback: Callback) {
        val request = Request.Builder().url(BASE_URL + url).build()
        client.newCall(request).enqueue(callback)
    }

    fun post(url: String, params: Map<String, String>, callback: Callback) {
        val jsonObject = JSONObject()
        for ((k, v) in params) {
            jsonObject.put(k, v)
        }
        val body = jsonObject.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder().url(BASE_URL + url).post(body).build()

        client.newCall(request).enqueue(callback)
    }
}
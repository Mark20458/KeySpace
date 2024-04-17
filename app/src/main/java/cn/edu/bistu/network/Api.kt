package cn.edu.bistu.network

import android.os.Handler
import android.os.Looper
import cn.edu.bistu.App
import cn.edu.bistu.util.ToastUtil
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

object Api {
    private val client = OkHttpClient()

    private const val BASE_URL = "https://suited-hardy-kodiak.ngrok-free.app/"

    fun get(url: String, handler: Handler) {
        val request = Request.Builder().url(BASE_URL + url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    ToastUtil.show("网络错误")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val string = response.body?.string()
                if (string == null) {
                    Handler(Looper.getMainLooper()).post {
                        ToastUtil.show("网络错误")
                    }
                    return
                }
                val json: JSONObject
                try {
                    json = JSONObject(string)
                } catch (e: Exception) {
                    Handler(Looper.getMainLooper()).post {
                        ToastUtil.show("网络错误")
                    }
                    return
                }
                handler.success(json)
            }

        })
    }

    fun post(url: String, params: Map<String, String>, handler: Handler) {
        val jsonObject = JSONObject()
        for ((k, v) in params) {
            jsonObject.put(k, v)
        }
        val body = jsonObject.toString().toRequestBody("application/json".toMediaType())

        val request = Request.Builder().url(BASE_URL + url).post(body).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    ToastUtil.show("网络错误")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val string = response.body?.string()
                if (string == null) {
                    Handler(Looper.getMainLooper()).post {
                        ToastUtil.show("网络错误")
                    }
                    return
                }
                val json: JSONObject
                try {
                    json = JSONObject(string)
                } catch (e: Exception) {
                    Handler(Looper.getMainLooper()).post {
                        ToastUtil.show("网络错误")
                    }
                    return
                }
                handler.success(json)
            }

        })
    }

    interface Handler {
        fun success(jsonObject: JSONObject)
    }
}
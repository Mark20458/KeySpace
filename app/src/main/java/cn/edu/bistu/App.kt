package cn.edu.bistu

import android.app.Application
import com.tencent.mmkv.MMKV

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // 初始化MMKV
        MMKV.initialize(this)
        instance = this
    }

    companion object {
        private lateinit var instance: App
        fun getInstance(): App {
            return instance
        }
    }
}
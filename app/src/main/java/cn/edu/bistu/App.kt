package cn.edu.bistu

import android.app.Application
import com.tencent.mmkv.MMKV

class App : Application() {
    /**
     * 当MainActivity失去焦点时是否展示为空白
     */
    var isBlank: Boolean = true
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
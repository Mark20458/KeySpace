package cn.edu.bistu

import android.app.Application
import cn.edu.bistu.database.database.DB
import com.tencent.mmkv.MMKV
import java.util.Stack

class App : Application() {
    var stack: Stack<Int> = Stack<Int>()
    lateinit var db: DB

    init {
        stack.push(-1)
    }

    override fun onCreate() {
        super.onCreate()
        // 初始化MMKV
        MMKV.initialize(this)
        db = DB.getDatabase(this)
        instance = this
    }

    companion object {
        private lateinit var instance: App
        fun getInstance(): App {
            return instance
        }
    }
}
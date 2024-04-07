package cn.edu.bistu.util

import com.google.gson.Gson

class JsonUtil {
    companion object {
        /**
         * 懒加载，确保只有使用的时候才会创建，单例模式
         */
        @Volatile
        var gson: Gson? = null
            get() {
                if (field == null) {
                    synchronized(this) {
                        if (field == null) {
                            field = Gson()
                        }
                    }
                }
                return field
            }
    }
}
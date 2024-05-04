package cn.edu.bistu.util

import cn.edu.bistu.database.model.Item
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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

        fun toString(item: Item): String {
            return gson!!.toJson(item)
        }

        fun toString(item: List<Item>): String {
            return gson!!.toJson(item)
        }

        fun toItem(json: String): Item {
            return gson!!.fromJson(json, Item::class.java)
        }

        fun toListItem(json: String): List<Item> {
            val type = object : TypeToken<List<Item>>() {}.type
            return gson!!.fromJson(json, type)
        }
    }
}
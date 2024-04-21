package cn.edu.bistu.util

import android.util.Base64
import java.security.MessageDigest

/**
 * 在这写一些扩展函数
 */
fun hash(message: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(message.toByteArray())
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}
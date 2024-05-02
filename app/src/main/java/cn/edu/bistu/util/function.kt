package cn.edu.bistu.util

import android.util.Base64
import android.util.TypedValue
import android.view.View
import cn.edu.bistu.App
import java.security.MessageDigest

/**
 * 在这写一些扩展函数
 */
fun hash(message: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(message.toByteArray())
    return Base64.encodeToString(bytes, Base64.NO_WRAP)
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Int.toDp(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        App.getInstance().resources.displayMetrics
    ).toInt()
}

fun generatePassword(
    length: Int,
    useUppercaseLetters: Boolean = true,
    useLowercaseLetters: Boolean = true,
    useDigits: Boolean = true,
    useSpecialChars: Boolean = true
): String {
    val uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val lowercaseLetters = "abcdefghijklmnopqrstuvwxyz"
    val digits = "0123456789"
    val specialChars = "!@#$%^&*()-_+=<>?"

    var allChars = String()
    if (useUppercaseLetters) allChars += uppercaseLetters
    if (useLowercaseLetters) allChars += lowercaseLetters
    if (useDigits) allChars += digits
    if (useSpecialChars) allChars += specialChars

    return (1..length)
        .map { allChars.random() }
        .joinToString("")
}
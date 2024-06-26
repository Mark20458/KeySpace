package cn.edu.bistu.util

import android.text.Editable
import android.util.TypedValue
import android.view.View
import cn.edu.bistu.App
import java.security.MessageDigest
import java.util.Base64

/**
 * 在这写一些扩展函数
 */
fun hash(message: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(message.toByteArray())
    return Base64.getEncoder().encodeToString(bytes)
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

fun String.toEditable(): Editable {
    return Editable.Factory.getInstance().newEditable(this)
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

fun ByteArray.toBase64(): String {
    return Base64.getEncoder().encodeToString(this)
}

fun String.toBase64ByteArray(): ByteArray {
    return Base64.getDecoder().decode(this)
}
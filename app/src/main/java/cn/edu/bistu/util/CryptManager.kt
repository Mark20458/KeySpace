package cn.edu.bistu.util

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * 加密和解密
 */
class CryptManager {
    companion object {
        private const val ITERATION_COUNT = 10000
        private const val KEY_LENGTH = 256
        private const val IV_LENGTH = 16

        fun encrypt(text: String, password: String, salt: String): String {
            val iv = generateIV()
            val key = generatePBKDFKey(password, salt)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
            val encryptedBytes = cipher.doFinal(text.toByteArray())
            val encryptedText = Base64.getEncoder().encodeToString(iv + encryptedBytes)
            return encryptedText
        }

        fun decrypt(encryptedText: String, password: String, salt: String): String {
            val encryptedBytes = Base64.getDecoder().decode(encryptedText)
            val iv = encryptedBytes.copyOf(IV_LENGTH)
            val key = generatePBKDFKey(password, salt)
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
            val decryptedBytes =
                cipher.doFinal(encryptedBytes.copyOfRange(IV_LENGTH, encryptedBytes.size))
            return String(decryptedBytes)
        }

        private fun generatePBKDFKey(
            password: String,
            salt: String,
        ): SecretKey {
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val spec =
                PBEKeySpec(password.toCharArray(), salt.toByteArray(), ITERATION_COUNT, KEY_LENGTH)
            return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
        }

        private fun generateIV(): ByteArray {
            val iv = ByteArray(IV_LENGTH)
            SecureRandom().nextBytes(iv)
            return iv
        }
    }
}
//
//fun main() {
//    val msg = "Hello world"
//    val password = "123456"
//    val salt = "123456"
//    val encrypt = CryptManager.encrypt(msg, password, salt)
//    val compressString = Compress.compressString(encrypt).toBase64()
//    val decompressString = Compress.decompressString(compressString.toBase64ByteArray())
//    val decrypt = CryptManager.decrypt(decompressString, password, salt)
//    println(compressString)
//    println(decompressString)
//    println("---------------")
//    println(encrypt)
//    println(decrypt)
//}
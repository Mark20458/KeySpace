package cn.edu.bistu.util

import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * 加密和解密
 * TODO iv向量可以设置为随机值
 */
class CryptManager {
    companion object {
        private const val ITERATION_COUNT = 10000
        private const val KEY_LENGTH = 256
        fun encrypt(text: String, password: String, salt: String): String {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(
                Cipher.ENCRYPT_MODE,
                generatePBKDFKey(password, salt),
                IvParameterSpec("1234567891234567".toByteArray())
            )
            val encryptedBytes = cipher.doFinal(text.toByteArray())
            return Base64.getEncoder().encodeToString(encryptedBytes)
        }

        fun decryptAES(encryptedText: String, password: String, salt: String): String {
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(
                Cipher.DECRYPT_MODE,
                generatePBKDFKey(password, salt),
                IvParameterSpec("1234567891234567".toByteArray())
            )
            val encryptedBytes = Base64.getDecoder().decode(encryptedText)
            val decryptedBytes = cipher.doFinal(encryptedBytes)
            return String(decryptedBytes)
        }

        private fun generatePBKDFKey(
            password: String,
            salt: String,
        ): SecretKey {
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val spec =
                PBEKeySpec(password.toCharArray(), salt.toByteArray(), ITERATION_COUNT, KEY_LENGTH)
            return SecretKeySpec(factory.generateSecret(spec).encoded, "AES");
        }
    }
}
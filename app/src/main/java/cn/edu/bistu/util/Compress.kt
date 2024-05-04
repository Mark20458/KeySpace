package cn.edu.bistu.util

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * 压缩和解压
 */
class Compress {
    companion object {
        fun compressString(str: String): ByteArray {
            val outputStream = ByteArrayOutputStream()
            val gzip = GZIPOutputStream(outputStream)
            gzip.write(str.toByteArray())
            gzip.close()
            return outputStream.toByteArray()
        }

        fun decompressString(compressed: ByteArray): String {
            val inputStream = ByteArrayInputStream(compressed)
            val gzip = GZIPInputStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len: Int
            while (gzip.read(buffer).also { len = it } > 0) {
                outputStream.write(buffer, 0, len)
            }
            gzip.close()
            return outputStream.toString()
        }
    }
}
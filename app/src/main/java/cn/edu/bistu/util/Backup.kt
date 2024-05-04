package cn.edu.bistu.util

import android.content.Context
import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import cn.edu.bistu.App
import kotlinx.coroutines.flow.first

class Backup {
    companion object {
        /**
         * 备份文件在该uri下
         */
        suspend fun backup(context: Context, folder: DocumentFile, fileName: String) {
            folder.createFile("application/zip", fileName)?.run {
                val list = App.getInstance().db.getItemDao().getAll().first()

                val password = SPUtil.getString(PreferencesKey.MASTER_PASSWORD, "123456")!!
                val salt = SPUtil.getString(PreferencesKey.SALT, "123456")!!
                val json = CryptManager.encrypt(JsonUtil.toString(list), password, salt)
                val bytes = Compress.compressString(json)

                val outputStream = context.contentResolver.openOutputStream(uri)
                outputStream?.run {
                    write(bytes)
                    flush()
                    close()
                }
            }
        }

        /**
         * 导入数据，将现有的全部删除，替换成文件中的
         */
        suspend fun import(context: Context, uri: Uri) {
            context.contentResolver.openInputStream(uri)?.run {
                val bytes = readBytes()
                if (bytes.isEmpty()) {
                    throw EmptyFileException("读取文件为空")
                }
                val password = SPUtil.getString(PreferencesKey.MASTER_PASSWORD, "123456")!!
                val salt = SPUtil.getString(PreferencesKey.SALT, "123456")!!
                val json = CryptManager.decrypt(Compress.decompressString(bytes), password, salt)
                val list = JsonUtil.toListItem(json)
                val dao = App.getInstance().db.getItemDao()
                dao.deleteAll()
                dao.insertItem(list)
                close()
            }
        }
    }
}

class EmptyFileException(msg: String) : Exception(msg)
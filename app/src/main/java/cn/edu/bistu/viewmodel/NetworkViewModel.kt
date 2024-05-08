package cn.edu.bistu.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.edu.bistu.network.Api
import cn.edu.bistu.util.PreferencesKey
import cn.edu.bistu.util.SPUtil
import cn.edu.bistu.util.ToastUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NetworkViewModel : ViewModel() {
    fun login(
        e_mail: String,
        password: String,
        masterPassword: String,
        callback: () -> Unit
    ) {
        val map = mapOf(
            "e_mail" to e_mail,
            "hash_password" to password,
            "master_password" to masterPassword
        )
        Api.post("login", map, object : Api.Handler {
            override fun success(jsonObject: JSONObject) {
                viewModelScope.launch(Dispatchers.Main) {
                    ToastUtil.show(jsonObject.getString("msg"))
                    if (jsonObject.getInt("code") != 101) return@launch
                    callback.invoke()
                    val array = jsonObject.getJSONArray("data")
                    SPUtil.saveString(PreferencesKey.TOKEN, array[0].toString())
                    SPUtil.saveString(PreferencesKey.SALT, array[1].toString())
                    SPUtil.saveString(PreferencesKey.ACCOUNT, e_mail)
                    SPUtil.saveString(PreferencesKey.MASTER_PASSWORD, masterPassword)
                    SPUtil.saveBoolean(PreferencesKey.LOGIN_STATE, true)
                }
            }
        })
    }

    fun register(
        e_mail: String,
        password: String,
        masterPassword: String,
        callback: (() -> Unit)? = null
    ) {
    }

    /**
     * 上传备份
     */
    fun upload(e_mail: String, data: String, callback: (() -> Unit)? = null) {
        val map = mapOf(
            "e_mail" to e_mail,
            "backup" to data
        )
        Api.post("backup", map, object : Api.Handler {
            override fun success(jsonObject: JSONObject) {
                viewModelScope.launch(Dispatchers.Main) {
                    ToastUtil.show(jsonObject.getString("msg"))
                    if (jsonObject.getInt("code") != 101) return@launch
                    callback?.invoke()
                }
            }
        }, token = true)
    }

    /**
     * 获取备份历史
     */
    fun getHistoryList(e_mail: String, callback: ((list: List<BackupData>) -> Unit)? = null) {
        Api.get("list/${e_mail}", object : Api.Handler {
            override fun success(jsonObject: JSONObject) {
                viewModelScope.launch(Dispatchers.Main) {
                    val backupDataList = arrayListOf<BackupData>()
                    ToastUtil.show(jsonObject.getString("msg"))
                    if (jsonObject.getInt("code") != 101) return@launch
                    val array = jsonObject.getJSONArray("data")
                    val formatter = DateTimeFormatter.ISO_DATE_TIME
                    for (i in 0 until array.length()) {
                        val backup = array.getJSONObject(i)
                        Log.i("gongz", backup.toString())
                        backupDataList.add(
                            BackupData(
                                id = backup.getInt("id"),
                                e_mail = backup.getString("e_mail"),
                                cTime = LocalDateTime.parse(
                                    backup.getString("create_time"),
                                    formatter
                                ),
                                data = backup.getString("backup")
                            )
                        )
                    }
                    callback?.invoke(backupDataList)
                }
            }
        }, token = true)
    }
}
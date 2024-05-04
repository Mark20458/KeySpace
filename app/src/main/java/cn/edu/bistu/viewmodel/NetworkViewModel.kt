package cn.edu.bistu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.edu.bistu.network.Api
import cn.edu.bistu.util.PreferencesKey
import cn.edu.bistu.util.SPUtil
import cn.edu.bistu.util.ToastUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

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
                    SPUtil.saveString(PreferencesKey.ACCOUNT, e_mail)
                    SPUtil.saveString(PreferencesKey.TOKEN, jsonObject.getString("data"))
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
        callback: () -> Unit
    ) {
    }
}
package cn.edu.bistu.viewmodel

import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.edu.bistu.App
import cn.edu.bistu.network.Api
import cn.edu.bistu.util.PreferencesKey
import cn.edu.bistu.util.ToastUtil
import cn.edu.bistu.util.dataStore
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
                viewModelScope.launch {
                    launch(Dispatchers.Main) {
                        ToastUtil.show(jsonObject.getString("msg"))
                        callback.invoke()
                    }
                    if (jsonObject.getInt("code") != 101) return@launch
                    App.getInstance().dataStore.edit {
                        it[PreferencesKey.TOKEN] = jsonObject.getString("data")
                        it[PreferencesKey.MASTER_PASSWORD] = masterPassword
                        it[PreferencesKey.LOGIN_STATE] = true
                    }

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
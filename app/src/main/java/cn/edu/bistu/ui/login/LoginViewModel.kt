package cn.edu.bistu.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.edu.bistu.network.Api
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class LoginViewModel : ViewModel() {

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> get() = _toastMessage

    @OptIn(DelicateCoroutinesApi::class)
    fun login(e_mail: String, password: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val map = mapOf("e_mail" to e_mail, "password" to password)
            Api.post("login", map, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    GlobalScope.launch(Dispatchers.Main) {
                        _toastMessage.value = "网络错误"
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val jsonObject = JSONObject(response.body?.string() ?: "")
                    GlobalScope.launch(Dispatchers.Main) {
                        _toastMessage.value = jsonObject.getString("msg") ?: "邮箱或密码错误"
                    }
                }

            })
        }
    }
}
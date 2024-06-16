package cn.edu.bistu.ui.login

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.edu.bistu.network.Api
import cn.edu.bistu.util.ToastUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


class LoginViewModel : ViewModel() {
    companion object {
        /**
         * 获取验证码间隔时长
         */
        const val TOTAL_TIME = 60 * 1000L

        /**
         * 倒计时间隔
         */
        const val COUNTDOWN_INTERVAL = 1000L
    }

    var isOver = MutableLiveData(true)
    var countDownTime = TOTAL_TIME
    var onTickTask: ((Long) -> Unit)? = null

    private val countDownTimer =
        object : CountDownTimer(TOTAL_TIME, COUNTDOWN_INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                countDownTime = millisUntilFinished
                onTickTask?.invoke(millisUntilFinished)
            }

            override fun onFinish() {
                isOver.value = true
                countDownTime = TOTAL_TIME
            }
        }

    fun sendVerifyCode(e_mail: String) {
        if (isOver.value == false) return
        isOver.value = false
        countDownTimer.start()
        getVerifyCode(e_mail)
    }

    /**
     * 获取验证码
     */
    fun getVerifyCode(e_mail: String) {
        Api.get("verify_code/${e_mail}", object : Api.Handler {
            override fun success(jsonObject: JSONObject) {
                viewModelScope.launch(Dispatchers.Main) {
                    ToastUtil.show(jsonObject.getString("msg"))
                    if (jsonObject.getInt("code") == 101) return@launch
                    else {
                        countDownTimer.cancel()
                        isOver.value = true
                        countDownTime = TOTAL_TIME
                    }
                }
            }
        })
    }
}
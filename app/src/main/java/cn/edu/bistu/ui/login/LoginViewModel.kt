package cn.edu.bistu.ui.login

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


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

    fun sendVerifyCode() {
        if (isOver.value == false) return
        isOver.value = false
        countDownTimer.start()
    }
}
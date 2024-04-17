package cn.edu.bistu.util

import android.widget.Toast
import cn.edu.bistu.App

class ToastUtil {
    companion object {
        fun show(message: String, length: Int = Toast.LENGTH_SHORT) {
            Toast.makeText(App.getInstance(), message, length).show()
        }
    }
}
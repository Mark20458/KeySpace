package cn.edu.bistu.util

import android.content.Context
import android.widget.Toast

class ToastUtil {
    companion object {
        fun show(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
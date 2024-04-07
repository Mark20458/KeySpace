package cn.edu.bistu.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class InputMethodUtil {
    companion object {
        fun show(context: Context, view: View) {
            val manager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, 0)
            manager.showSoftInput(view, 0)
        }

        fun hide(context: Context, view: View) {
            val manager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}
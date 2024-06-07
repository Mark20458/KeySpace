package cn.edu.bistu.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import cn.edu.bistu.databinding.DialogTipBinding

class TipDialog : DialogFragment() {
    private lateinit var mBind: DialogTipBinding
    private var title: String = ""
    private var confirmCallback: (() -> Unit)? = null
    private var cancelCallback: (() -> Unit)? = null

    companion object {
        fun getInstance(title: String): TipDialog {
            val dialog = TipDialog()
            dialog.title = title
            return dialog
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DialogTipBinding.inflate(inflater, container, false)
        mBind.confirm.setOnClickListener {
            confirmCallback?.invoke()
            dismiss()
        }
        mBind.cancel.setOnClickListener {
            dismiss()
            cancelCallback?.invoke()
        }
        mBind.title.text = title
        return mBind.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.run {
            setBackgroundDrawableResource(android.R.color.transparent)
            val params = attributes
            params.width = (resources.displayMetrics.widthPixels * 0.9).toInt()
            params.height = WindowManager.LayoutParams.WRAP_CONTENT
            attributes = params
        }
    }

    fun setConfirmCallback(callback: () -> Unit): TipDialog {
        confirmCallback = callback
        return this
    }

    fun setCancelCallback(callback: () -> Unit): TipDialog {
        cancelCallback = callback
        return this
    }
}
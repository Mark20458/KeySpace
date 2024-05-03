package cn.edu.bistu.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        mBind.cancle.setOnClickListener {
            dismiss()
            cancelCallback?.invoke()
        }
        mBind.title.text = title
        return mBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 获取对话框窗口
        val dialogWindow = dialog?.window

        dialogWindow?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
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
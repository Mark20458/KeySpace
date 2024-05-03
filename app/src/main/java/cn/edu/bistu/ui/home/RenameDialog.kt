package cn.edu.bistu.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import cn.edu.bistu.databinding.DialogRenameBinding
import cn.edu.bistu.util.InputMethodUtil
import cn.edu.bistu.util.toEditable

/**
 * 创建文件夹的弹窗
 */
class RenameDialog : DialogFragment() {
    private lateinit var mBind: DialogRenameBinding
    private var name: String = ""
    private var confirmCallback: ((name: String) -> Unit)? = null

    companion object {
        fun getInstance(name: String): RenameDialog {
            val dialog = RenameDialog()
            dialog.name = name
            return dialog
        }
    }

    fun setConfirmCallback(callback: (name: String) -> Unit): RenameDialog {
        confirmCallback = callback
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DialogRenameBinding.inflate(inflater, container, false)
        mBind.title.text = name.toEditable()
        mBind.confirm.setOnClickListener {
            if (mBind.folder.text.toString().isBlank()) {
                mBind.folderLayout.error = "文件夹名称不能为空"
                return@setOnClickListener
            }
            confirmCallback?.invoke(mBind.folder.text.toString())
            dismiss()
        }
        mBind.folder.requestFocus()
        mBind.folder.postDelayed({
            InputMethodUtil.show(requireContext(), mBind.folder)
        }, 50)
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
}
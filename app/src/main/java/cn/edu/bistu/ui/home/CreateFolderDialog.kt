package cn.edu.bistu.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import cn.edu.bistu.App
import cn.edu.bistu.databinding.DialogCreateFolderBinding

/**
 * 创建文件夹的弹窗
 */
class CreateFolderDialog : DialogFragment() {
    private lateinit var mBind: DialogCreateFolderBinding
    private var confirmCallback: ((name: String) -> Unit)? = null

    companion object {
        fun getInstance(): CreateFolderDialog {
            return CreateFolderDialog()
        }
    }

    fun setConfirmCallback(callback: (name: String) -> Unit): CreateFolderDialog {
        confirmCallback = callback
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = DialogCreateFolderBinding.inflate(inflater, container, false)
        mBind.title.text = App.getInstance().stack.peek().name
        mBind.confirm.setOnClickListener {
            if (mBind.folder.text.toString().isBlank()) {
                mBind.folderLayout.error = "文件夹名称不能为空"
                return@setOnClickListener
            }
            confirmCallback?.invoke(mBind.folder.text.toString())
            dismiss()
        }
        return mBind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 获取对话框窗口
        val dialogWindow = dialog?.window

        // 设置对话框的宽度为屏幕宽度的一半
        dialogWindow?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
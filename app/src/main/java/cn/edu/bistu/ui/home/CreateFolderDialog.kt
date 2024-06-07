package cn.edu.bistu.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import cn.edu.bistu.App
import cn.edu.bistu.databinding.DialogCreateFolderBinding
import cn.edu.bistu.util.InputMethodUtil

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
        mBind.folder.requestFocus()
        mBind.folder.postDelayed({
            InputMethodUtil.show(requireContext(), mBind.folder)
        }, 100)
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
}
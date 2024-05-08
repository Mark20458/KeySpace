package cn.edu.bistu.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cn.edu.bistu.databinding.BottomSheetFragmentSelectHistoryBackupDataBinding
import cn.edu.bistu.util.gone
import cn.edu.bistu.util.toDp
import cn.edu.bistu.util.visible
import cn.edu.bistu.viewmodel.BackupData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * 选择历史备份数据底部弹窗
 */
class SelectHistoryBackupBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var mBind: BottomSheetFragmentSelectHistoryBackupDataBinding
    private lateinit var list: List<BackupData>
    private var clickItemCallback: ((item: BackupData) -> Unit)? = null

    companion object {
        fun getInstance(list: List<BackupData>): SelectHistoryBackupBottomSheetFragment {
            val dialog = SelectHistoryBackupBottomSheetFragment()
            dialog.list = list
            return dialog
        }
    }

    fun setClickItemCallback(callback: ((item: BackupData) -> Unit)): SelectHistoryBackupBottomSheetFragment {
        clickItemCallback = callback
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind =
            BottomSheetFragmentSelectHistoryBackupDataBinding.inflate(inflater, container, false)
        val layoutParams = mBind.root.layoutParams ?: ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            600.toDp(),
        )
        mBind.root.layoutParams = layoutParams
        initView()
        return mBind.root
    }

    private fun initView() {
        if (list.isEmpty()) {
            mBind.empty.visible()
            mBind.content.gone()
            return
        } else {
            mBind.empty.gone()
            mBind.content.visible()
            mBind.content.layoutManager = LinearLayoutManager(requireActivity())
            val adapter = BackupDataAdapter(list)
            adapter.setClickItemCallback {
                clickItemCallback?.invoke(it)
                dismiss()
            }
            mBind.content.adapter = adapter
        }

    }
}
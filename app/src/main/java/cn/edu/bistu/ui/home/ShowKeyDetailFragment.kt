package cn.edu.bistu.ui.home

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.edu.bistu.R
import cn.edu.bistu.database.model.Item
import cn.edu.bistu.databinding.FragmentShowKeyDetailBinding
import cn.edu.bistu.ui.dialog.TipDialog
import cn.edu.bistu.util.InputMethodUtil
import cn.edu.bistu.util.JsonUtil
import cn.edu.bistu.util.ToastUtil
import cn.edu.bistu.util.toEditable
import cn.edu.bistu.viewmodel.DBViewModel
import java.text.DateFormat
import java.util.Locale

class ShowKeyDetailFragment : Fragment() {
    private lateinit var mBind: FragmentShowKeyDetailBinding
    private lateinit var item: Item
    private lateinit var viewModel: DBViewModel

    /**
     * 默认不可编辑
     */
    private var editor: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val json = arguments?.getString("item")
        if (json.isNullOrBlank()) {
            ToastUtil.show("内容错误,请重试")
            requireActivity().onBackPressedDispatcher.onBackPressed()
        } else {
            item = JsonUtil.toItem(json)
        }
        viewModel = ViewModelProvider(requireActivity())[DBViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentShowKeyDetailBinding.inflate(inflater, container, false)

        initView()
        initListener()
        return mBind.root
    }


    private fun initView() {
        mBind.title.text = item.name
        mBind.name.text = item.name.toString().toEditable()
        mBind.username.text = item.username.toString().toEditable()
        mBind.password.text = item.password.toString().toEditable()
        mBind.url.text = item.url.toString().toEditable()
        mBind.websiteName.text = item.websiteName.toString().toEditable()
        mBind.note.text = item.note.toString().toEditable()
        val format = DateFormat.getDateTimeInstance(
            DateFormat.SHORT,
            DateFormat.MEDIUM,
            Locale.getDefault()
        )
        mBind.cTime.text = format.format(item.cTime).toString().toEditable()
        if (item.mTime == null) {
            mBind.mTime.text = "".toEditable()
        } else {
            mBind.mTime.text = format.format(item.mTime).toString().toEditable()
        }
    }

    private fun initListener() {
        mBind.back.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        mBind.modify.setOnClickListener {
            editor = !editor
            if (editor) {
                // 从不可编辑状态变为可编辑状态
                resetView()
                mBind.name.requestFocus()
                mBind.name.setSelection(mBind.name.text.length)
                InputMethodUtil.show(requireActivity(), mBind.name)
            } else {
                if (mBind.name.text.toString().isBlank() || mBind.password.text.toString()
                        .isBlank()
                ) {
                    ToastUtil.show("名称或密码不能为空！")
                    resetView()
                    return@setOnClickListener
                }
                TipDialog.getInstance("您确定要修改吗？").setConfirmCallback {
                    val newItem = item.copy(
                        name = mBind.name.text.toString(),
                        username = mBind.username.text.toString(),
                        password = mBind.password.text.toString(),
                        url = mBind.url.text.toString(),
                        websiteName = mBind.websiteName.text.toString(),
                        note = mBind.note.text.toString(),
                        cTime = item.cTime,
                        mTime = System.currentTimeMillis()
                    )
                    viewModel.update(newItem)
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }.show(requireActivity().supportFragmentManager, "update")
            }
        }
    }

    private fun resetView() {
        if (editor) {
            mBind.run {
                modify.setImageResource(R.drawable.save)
                name.isEnabled = true
                username.isEnabled = true
                password.isEnabled = true
                url.isEnabled = true
                websiteName.isEnabled = true
                note.isEnabled = true

                password.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
            }
        } else {
            initView()
            mBind.modify.setImageResource(R.drawable.generate_black)
            mBind.run {
                name.isEnabled = false
                username.isEnabled = false
                password.isEnabled = false
                url.isEnabled = false
                websiteName.isEnabled = false
                note.isEnabled = false

                password.inputType =
                    InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            }
        }
    }
}
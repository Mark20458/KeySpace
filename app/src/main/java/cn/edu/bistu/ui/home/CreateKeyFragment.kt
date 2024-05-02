package cn.edu.bistu.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cn.edu.bistu.App
import cn.edu.bistu.database.model.Item
import cn.edu.bistu.databinding.FragmentCreateBinding
import cn.edu.bistu.util.ToastUtil
import cn.edu.bistu.viewmodel.DBViewModel

/**
 * 创建密钥的页面
 */
class CreateKeyFragment : Fragment() {
    private lateinit var mBind: FragmentCreateBinding

    private lateinit var viewModel: DBViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentCreateBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[DBViewModel::class.java]

        initView()
        initListener()
        return mBind.root
    }

    private fun initView() {
    }

    private fun initListener() {
        mBind.back.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        mBind.name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                val name = s.toString()
                if (name.isBlank()) {
                    mBind.nameLayout.error = "名称不能为空"
                } else {
                    mBind.nameLayout.error = null
                }
            }
        })

        mBind.password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.isBlank()) {
                    mBind.passwordLayout.error = "密码不能为空"
                } else {
                    mBind.passwordLayout.error = null
                }
            }
        })
        mBind.save.setOnClickListener {
            val name = mBind.name.text.toString()
            val password = mBind.password.text.toString()
            if (!check(name, password)) {
                return@setOnClickListener
            }
            val item = Item(
                name = name,
                username = mBind.username.text.toString(),
                password = password,
                url = mBind.url.text.toString(),
                websiteName = mBind.websiteName.text.toString(),
                note = mBind.note.text.toString(),
                isKey = true,
                parentId = App.getInstance().stack.peek().id
            )
            viewModel.save(item)
            requireActivity().onBackPressedDispatcher.onBackPressed()

        }
        mBind.generatePassword.setOnClickListener {
            GeneratePasswordBottomSheetFragment.getInstance().setAcceptCallback {
                mBind.password.text = Editable.Factory.getInstance().newEditable(it)
            }.show(childFragmentManager, "GeneratePasswordBottomSheetFragment")
        }
    }

    private fun check(name: String, password: String): Boolean {
        var flag = true
        if (name.isBlank()) {
            mBind.nameLayout.error = "名称不能为空"
            flag = false
        } else {
            mBind.nameLayout.error = null
        }

        if (password.isBlank()) {
            mBind.passwordLayout.error = "密码不能为空"
            flag = false
        } else {
            mBind.passwordLayout.error = null
        }
        if (!flag)
            ToastUtil.show("请按提示进行操作")
        return flag
    }
}
package cn.edu.bistu.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.edu.bistu.databinding.FragmentCreateBinding

class CreateFragment : Fragment() {
    private lateinit var mBind: FragmentCreateBinding

    private var isKey: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isKey = arguments?.getBoolean("isKey") ?: isKey
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentCreateBinding.inflate(inflater, container, false)

        initView()
        initListener()
        return mBind.root
    }

    private fun initView() {
        mBind.title.text = if (isKey) "创建密钥" else "创建文件夹"
        mBind.a.text = if (isKey) "password" else "folder"
    }

    private fun initListener() {
        mBind.back.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}
package cn.edu.bistu.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import cn.edu.bistu.R
import cn.edu.bistu.databinding.FragmentStartBinding

/**
 * 第一次登录选择登录或注册
 */
class StartFragment : Fragment() {

    companion object {
        const val TAG = "start"

        fun newInstance() = StartFragment()
    }

    private lateinit var mBind: FragmentStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentStartBinding.inflate(inflater)

        // 点击登录按钮
        mBind.login.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("login", true)
            val controller = Navigation.findNavController(requireActivity(), R.id.container)
            controller.navigate(R.id.action_startFragment_to_loginFragment, bundle)
        }
        // 点击注册按钮
        mBind.register.setOnClickListener {
            val bundle = Bundle()
            bundle.putBoolean("login", false)
            val controller = Navigation.findNavController(requireActivity(), R.id.container)
            controller.navigate(R.id.action_startFragment_to_loginFragment, bundle)
        }
        return mBind.root
    }
}
package cn.edu.bistu.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import cn.edu.bistu.R
import cn.edu.bistu.databinding.FragmentLoginBinding
import cn.edu.bistu.util.ToastUtil

class LoginFragment : Fragment() {

    companion object {
        const val TAG = "login"
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var mBind: FragmentLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentLoginBinding.inflate(inflater)
        mBind.back.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.container).popBackStack()
        }
        mBind.confirm.setOnClickListener {
            viewModel.login(mBind.email.text.toString(), mBind.password.text.toString())
        }

        viewModel.toastMessage.observe(viewLifecycleOwner, Observer {
            ToastUtil.show(requireContext(), it)
        })
        return mBind.root
    }

}
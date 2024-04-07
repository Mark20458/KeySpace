package cn.edu.bistu.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import cn.edu.bistu.R
import cn.edu.bistu.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    companion object {
        const val TAG = "register"
        fun newInstance() = RegisterFragment()
    }

    private lateinit var mBind: FragmentRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentRegisterBinding.inflate(inflater)
        mBind.back.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.container).popBackStack()
        }
        return mBind.root
    }
}
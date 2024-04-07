package cn.edu.bistu.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import cn.edu.bistu.R
import cn.edu.bistu.databinding.FragmentStartBinding

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

        mBind.login.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.container)
                .navigate(R.id.action_startFragment_to_loginFragment)
        }

        mBind.register.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.container)
                .navigate(R.id.action_startFragment_to_registerFragment)
        }
        return mBind.root
    }
}
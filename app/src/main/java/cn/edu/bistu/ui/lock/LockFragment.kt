package cn.edu.bistu.ui.lock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import cn.edu.bistu.R
import cn.edu.bistu.databinding.FragmentLockBinding

class LockFragment : Fragment() {
    private lateinit var mBind: FragmentLockBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentLockBinding.inflate(inflater)
        mBind.a.setOnClickListener {
            Navigation.findNavController(requireActivity(), R.id.container)
                .navigate(R.id.action_lockFragment_to_mainFragment)
        }
        return mBind.root
    }
}
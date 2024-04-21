package cn.edu.bistu.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.edu.bistu.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var mBind: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentHomeBinding.inflate(inflater)
        return mBind.root
    }
}
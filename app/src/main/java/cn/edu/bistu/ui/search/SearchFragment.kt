package cn.edu.bistu.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.edu.bistu.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    private lateinit var mBind: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentSearchBinding.inflate(inflater)
        return mBind.root
    }
}
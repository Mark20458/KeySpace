package cn.edu.bistu.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import cn.edu.bistu.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private lateinit var mBind: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentSettingsBinding.inflate(inflater)
        return mBind.root
    }
}
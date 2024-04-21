package cn.edu.bistu.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import cn.edu.bistu.R
import cn.edu.bistu.databinding.FragmentMainBinding
import cn.edu.bistu.ui.home.HomeFragment
import cn.edu.bistu.ui.search.SearchFragment
import cn.edu.bistu.ui.settings.SettingsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainFragment : Fragment() {
    private lateinit var mBind: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentMainBinding.inflate(inflater)

        val fragments = listOf(HomeFragment(), SearchFragment(), SettingsFragment())
        val tabs = listOf("首页", "搜索", "设置")
        val tabIcons = listOf(
            R.drawable.home,
            R.drawable.search,
            R.drawable.settings
        )
        mBind.viewpager.adapter = object : FragmentStateAdapter(childFragmentManager, lifecycle) {
            override fun getItemCount(): Int {
                return tabs.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position]
            }

        }

        TabLayoutMediator(
            mBind.tabLayout, mBind.viewpager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.setIcon(tabIcons[position])
            tab.text = tabs[position]
        }.attach()
        return mBind.root
    }
}
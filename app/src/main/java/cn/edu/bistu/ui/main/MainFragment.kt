package cn.edu.bistu.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import cn.edu.bistu.App
import cn.edu.bistu.R
import cn.edu.bistu.databinding.FragmentMainBinding
import cn.edu.bistu.ui.home.HomeFragment
import cn.edu.bistu.ui.search.SearchFragment
import cn.edu.bistu.ui.settings.SettingsFragment
import cn.edu.bistu.util.ToastUtil
import cn.edu.bistu.viewmodel.DBViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainFragment : Fragment() {
    private lateinit var mBind: FragmentMainBinding
    private lateinit var dispatcher: OnBackPressedDispatcher

    private lateinit var callback: OnBackPressedCallback

    private lateinit var viewModel: DBViewModel

    /**
     * 连续按两次返回键退出应用（backPressCount == 2）
     */
    private var backPressJob: Job? = null

    /**
     * 返回HomeFragment页面后，连续按返回键的次数
     */
    private var backPressCount: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentMainBinding.inflate(inflater)

        viewModel = ViewModelProvider(requireActivity())[DBViewModel::class.java]
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

        mBind.viewpager.isUserInputEnabled = true
        TabLayoutMediator(
            mBind.tabLayout, mBind.viewpager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.setIcon(tabIcons[position])
            tab.text = tabs[position]
        }.attach()

        mBind.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position != 0) {
                    backPressCount = 0
                }
            }
        })
        initBackPress()
        return mBind.root
    }

    /**
     * 返回逻辑：
     * 1. 是否返回主tab
     * 2. 主tab是否是根节点
     * 3. 连续按了两次
     */
    private fun initBackPress() {
        dispatcher = requireActivity().onBackPressedDispatcher
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mBind.viewpager.currentItem != 0) {
                    mBind.viewpager.currentItem = 0
                } else if (App.getInstance().stack.size > 1) {
                    App.getInstance().stack.pop()
                    viewModel.updateList()
                } else {
                    backPressCount++
                    if (backPressCount == 2) {
                        requireActivity().finish()
                        return
                    } else {
                        ToastUtil.show("再按一次退出应用")
                        backPressJob?.cancel()
                        backPressJob = lifecycleScope.launch {
                            delay(3000)
                            backPressCount = 0
                        }
                        backPressJob?.start()
                    }
                }
            }
        }
        dispatcher.addCallback(viewLifecycleOwner, callback)
        callback.isEnabled = true
    }

}
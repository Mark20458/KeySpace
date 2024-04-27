package cn.edu.bistu.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.bistu.App
import cn.edu.bistu.R
import cn.edu.bistu.databinding.FragmentHomeBinding
import cn.edu.bistu.util.gone
import cn.edu.bistu.util.visible
import cn.edu.bistu.viewmodel.DBViewModel

class HomeFragment : Fragment() {
    private lateinit var mBind: FragmentHomeBinding

    private lateinit var viewModel: DBViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentHomeBinding.inflate(inflater)
        viewModel = ViewModelProvider(requireActivity())[DBViewModel::class.java]
        viewModel.updateList()



        initView()
        initListener()

        return mBind.root
    }

    private fun initView() {
        mBind.title.text = App.getInstance().stack.peek().name

        viewModel.list.observe(viewLifecycleOwner) {
            if (it != null) {
                mBind.empty.gone()
                mBind.content.visible()
                mBind.content.layoutManager = LinearLayoutManager(requireActivity())
                val adapter = ItemAdapter(it)
                adapter.clickKey = {
                    // TODO 进入查看密钥页面
                }

                adapter.clickFolder = {
                    // TODO 进入查看文件夹页面
                }

                adapter.longClickItem = {
                    // TODO 长按可以进行删除，复制等操作（下拉菜单）
                }
                mBind.content.adapter = adapter
            } else {
                mBind.empty.visible()
                mBind.content.gone()
            }
        }
    }

    private fun initListener() {
        mBind.content.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var flag: Boolean = true

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if ((dy > 0 && flag) || (dy < 0 && !flag)) {
                    flag = !flag
                    if (mBind.create.isExtended) {
                        mBind.create.shrink()
                    } else {
                        mBind.create.extend()
                    }
                }
            }
        })

        mBind.back.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        mBind.create.setOnClickListener {
            val popupMenu = PopupMenu(requireActivity(), mBind.create)
            popupMenu.menuInflater.inflate(R.menu.create_type, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {

                when (it.itemId) {
                    R.id.password -> {
                        val bundle = Bundle()
                        bundle.putBoolean("isKey", true)
                        Navigation.findNavController(requireActivity(), R.id.container)
                            .navigate(R.id.action_mainFragment_to_createFragment, bundle)
                    }

                    R.id.folder -> {
                        val bundle = Bundle()
                        bundle.putBoolean("isKey", false)
                        Navigation.findNavController(requireActivity(), R.id.container)
                            .navigate(R.id.action_mainFragment_to_createFragment, bundle)
                    }
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
    }
}
package cn.edu.bistu.ui.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
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
import cn.edu.bistu.database.model.Item
import cn.edu.bistu.databinding.FragmentHomeBinding
import cn.edu.bistu.ui.dialog.TipDialog
import cn.edu.bistu.util.JsonUtil
import cn.edu.bistu.util.ToastUtil
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
            if (!it.isNullOrEmpty()) {
                mBind.empty.gone()
                mBind.content.visible()
                mBind.content.layoutManager = LinearLayoutManager(requireActivity())
                val adapter = ItemAdapter(it)
                // 点击密钥跳转到密钥详情
                adapter.clickKey = {
                    val bundle = Bundle()
                    bundle.putString("item", JsonUtil.toString(it))
                    Navigation.findNavController(requireActivity(), R.id.container)
                        .navigate(R.id.action_mainFragment_to_showKeyDetailFragment, bundle)
                }

                // 点击文件夹,跳转文件夹目录下
                adapter.clickFolder = { item ->
                    App.getInstance().stack.push(item)
                    viewModel.updateList()
                }

                // 长按元素,弹出菜单可以进行删除
                adapter.longClickItem = { item, view ->
                    view.setBackgroundColor(Color.parseColor("#64B5F6"))
                    val popupMenu = PopupMenu(requireActivity(), view)
                    popupMenu.setOnDismissListener {
                        view.setBackgroundColor(Color.parseColor("#9EDDFF"))
                    }
                    popupMenu.gravity = Gravity.END
                    if (item.isKey)
                        setPopupMenuKey(popupMenu, item)
                    else
                        setPopupMenuFolder(popupMenu, item)
                    popupMenu.show()
                }
                adapter.setKeyEndIconClickCallback { item ->
                    copy(item)
                }
                adapter.setFolderEndIconClickCallback { item ->
                    App.getInstance().stack.push(item)
                    viewModel.updateList()
                }
                mBind.content.adapter = adapter
            } else {
                mBind.empty.visible()
                mBind.content.gone()
            }
            update()
        }
    }

    private fun setPopupMenuFolder(popupMenu: PopupMenu, item: Item) {
        popupMenu.menuInflater.inflate(R.menu.long_click_folder_operation, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.onlyDelete -> {
                    TipDialog.getInstance("您确定要删除${item.name}吗?\n该目录下的子项不会被删除")
                        .setConfirmCallback {
                            viewModel.deleteOnlyFolder(item)
                            ToastUtil.show("删除成功")
                        }.show(childFragmentManager, "delete")
                }

                R.id.deleteAll -> {
                    TipDialog.getInstance("您确定要删除${item.name}文件夹及其子项吗?")
                        .setConfirmCallback {
                            viewModel.deleteFolderAndChildren(item)
                            ToastUtil.show("删除成功")
                        }.show(childFragmentManager, "delete")
                }
                // TODO 移动文件夹
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun setPopupMenuKey(popupMenu: PopupMenu, item: Item) {
        popupMenu.menuInflater.inflate(R.menu.long_click_key_operation, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.delete -> {
                    TipDialog.getInstance("您确定要删除${item.name}吗?").setConfirmCallback {
                        viewModel.delete(item)
                        ToastUtil.show("删除成功")
                        viewModel.updateList()
                    }.show(childFragmentManager, "delete")
                }

                R.id.copy -> {
                    copy(item)
                }
            }
            return@setOnMenuItemClickListener true
        }
    }

    /**
     * 复制内容到粘贴板上
     */
    private fun copy(item: Item) {
        val manager =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("password", item.password)
        manager.setPrimaryClip(clip)
        ToastUtil.show("复制成功")
    }

    private fun update() {
        mBind.title.text = App.getInstance().stack.peek().name
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
                        Navigation.findNavController(requireActivity(), R.id.container)
                            .navigate(R.id.action_mainFragment_to_createFragment)
                    }

                    R.id.folder -> {
                        CreateFolderDialog.getInstance()
                            .setConfirmCallback {
                                val item = Item(
                                    name = it,
                                    isKey = false,
                                    parentId = App.getInstance().stack.peek().id
                                )
                                viewModel.save(item)
                                viewModel.updateList()
                            }.show(childFragmentManager, "CreateFolderDialog")
                    }
                }
                return@setOnMenuItemClickListener true
            }
            popupMenu.show()
        }
    }
}
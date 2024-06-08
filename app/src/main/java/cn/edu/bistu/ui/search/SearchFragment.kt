package cn.edu.bistu.ui.search

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import cn.edu.bistu.App
import cn.edu.bistu.R
import cn.edu.bistu.database.model.Item
import cn.edu.bistu.databinding.FragmentSearchBinding
import cn.edu.bistu.ui.dialog.TipDialog
import cn.edu.bistu.ui.home.ItemAdapter
import cn.edu.bistu.ui.home.RenameDialog
import cn.edu.bistu.util.InputMethodUtil
import cn.edu.bistu.util.JsonUtil
import cn.edu.bistu.util.ToastUtil
import cn.edu.bistu.util.gone
import cn.edu.bistu.util.toEditable
import cn.edu.bistu.util.visible
import cn.edu.bistu.viewmodel.DBViewModel

class SearchFragment : Fragment() {
    private lateinit var mBind: FragmentSearchBinding
    private lateinit var viewModel: DBViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentSearchBinding.inflate(inflater)
        viewModel = ViewModelProvider(requireActivity())[DBViewModel::class.java]
        initView()
        initListener()
        return mBind.root
    }

    private fun reset() {
        mBind.searchInput.text = "".toEditable()
        mBind.iconClear.gone()
        mBind.searchContent.gone()
        mBind.empty.visible()
    }

    private fun initView() {
        reset()
        viewModel.searchList.observe(viewLifecycleOwner) {
            if (it.isEmpty()) {
                mBind.searchContent.gone()
                mBind.empty.visible()
            } else {
                mBind.searchContent.visible()
                mBind.empty.gone()
                mBind.searchContent.layoutManager = LinearLayoutManager(requireActivity())
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
                    requireActivity().onBackPressedDispatcher.onBackPressed()
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
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                mBind.searchContent.adapter = adapter
            }
        }
    }

    private fun initListener() {
        mBind.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s == null) return
                if (s.toString().isNotEmpty()) {
                    mBind.iconClear.visible()
                    viewModel.updateSearchList(s.toString())
                } else {
                    mBind.iconClear.gone()
                    mBind.searchContent.gone()
                    mBind.empty.visible()
                }
            }
        })

        mBind.iconClear.setOnClickListener {
            reset()
        }
    }

    private fun setPopupMenuFolder(popupMenu: PopupMenu, item: Item) {
        popupMenu.menuInflater.inflate(R.menu.long_click_folder_operation, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.rename -> {
                    RenameDialog.getInstance(item.name.toString())
                        .setConfirmCallback {
                            viewModel.update(item.copy(name = it))
                            viewModel.updateSearchList(mBind.searchInput.text.toString())
                        }.show(childFragmentManager, "CreateFolderDialog")
                }

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
                        viewModel.updateSearchList(mBind.searchInput.text.toString())
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

    override fun onResume() {
        super.onResume()
        mBind.searchInput.requestFocus()
        mBind.searchInput.postDelayed({
            InputMethodUtil.show(requireContext(), mBind.searchInput)
        }, 100)
    }

    override fun onPause() {
        super.onPause()
        viewModel.oldKeyword = ""
    }
}
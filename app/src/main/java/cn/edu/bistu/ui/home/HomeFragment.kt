package cn.edu.bistu.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.edu.bistu.databinding.FragmentHomeBinding
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

        viewModel.list.observe(viewLifecycleOwner) {
            if (it != null) {
                mBind.content.layoutManager = LinearLayoutManager(requireActivity())
                mBind.content.adapter = ItemAdapter(it)
            } else {
                mBind.content.adapter = null
            }
        }

        mBind.content.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var flag: Boolean = true
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
//                    if (mBind.create.isExtended) {
//                        mBind.create.shrink()
//                    } else {
//                        mBind.create.extend()
//                    }
//                }
//            }

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
        return mBind.root
    }
}
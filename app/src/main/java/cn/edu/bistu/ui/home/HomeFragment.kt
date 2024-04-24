package cn.edu.bistu.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.edu.bistu.App
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
        App.getInstance().stack.push(5)
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
        return mBind.root
    }
}
package cn.edu.bistu.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cn.edu.bistu.R
import cn.edu.bistu.database.model.Item
import cn.edu.bistu.databinding.ItemPasswordBinding

class ItemAdapter(private val list: List<Item>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(val mBind: ItemPasswordBinding) : ViewHolder(mBind.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = ItemPasswordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]
        holder.mBind.itemName.text = position.toString()
        if (item.isKey) {
            holder.mBind.type.setImageResource(R.drawable.key)
        } else {
            holder.mBind.type.setImageResource(R.drawable.folder)
        }
    }
}
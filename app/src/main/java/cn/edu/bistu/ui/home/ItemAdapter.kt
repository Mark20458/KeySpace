package cn.edu.bistu.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import cn.edu.bistu.R
import cn.edu.bistu.database.model.Item
import cn.edu.bistu.databinding.ItemPasswordBinding

class ItemAdapter(private val list: List<Item>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    class ItemViewHolder(val mBind: ItemPasswordBinding) : ViewHolder(mBind.root)

    /**
     * 点击密钥的回调
     */
    var clickKey: ((item: Item) -> Unit)? = null

    /**
     * 点击文件夹的回调
     */
    var clickFolder: ((item: Item) -> Unit)? = null

    /**
     * 长按事件
     */
    var longClickItem: ((item: Item, view: View) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = ItemPasswordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = list[position]
        holder.mBind.itemName.text = item.name
        if (item.isKey) {
            holder.mBind.root.setOnClickListener {
                clickKey?.invoke(item)
            }
            holder.mBind.type.setImageResource(R.drawable.key)
        } else {
            holder.mBind.root.setOnClickListener {
                clickFolder?.invoke(item)
            }
            holder.mBind.type.setImageResource(R.drawable.folder)
        }
        holder.mBind.root.setOnLongClickListener {
            longClickItem?.invoke(item, it)
            return@setOnLongClickListener true
        }
    }
}
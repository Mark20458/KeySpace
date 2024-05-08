package cn.edu.bistu.ui.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.edu.bistu.databinding.ItemBackupDataBinding
import cn.edu.bistu.util.toEditable
import cn.edu.bistu.viewmodel.BackupData
import java.time.format.DateTimeFormatter

class BackupDataAdapter(private val list: List<BackupData>) :
    RecyclerView.Adapter<BackupDataAdapter.BackupDataViewHolder>() {
    class BackupDataViewHolder(val mBind: ItemBackupDataBinding) :
        RecyclerView.ViewHolder(mBind.root)

    private var clickItemCallback: ((item: BackupData) -> Unit)? = null

    fun setClickItemCallback(callback: ((item: BackupData) -> Unit)) {
        clickItemCallback = callback
    }

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BackupDataViewHolder {
        val view = ItemBackupDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BackupDataViewHolder(view)
    }


    override fun onBindViewHolder(holder: BackupDataViewHolder, position: Int) {
        val item = list[position]
        holder.mBind.run {
            id.text = (position + 1).toString().toEditable()
            cTime.text = item.cTime.format(formatter).toEditable()
            confirm.setOnClickListener {
                clickItemCallback?.invoke(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
package cn.edu.bistu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.edu.bistu.App
import cn.edu.bistu.database.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Stack

class DBViewModel : ViewModel() {
    private var _list = MutableLiveData<List<Item>>()
    val list: LiveData<List<Item>?> get() = _list

    fun updateList() {
        viewModelScope.launch {
            _list.value =
                App.getInstance().db.getItemDao()
                    .getItemByParentId(App.getInstance().stack.peek().id)
                    .first()
        }
    }

    fun save(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            App.getInstance().db.getItemDao().insertItem(item)
        }
    }

    fun update(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            App.getInstance().db.getItemDao().updateItem(item)
        }
    }

    /**
     * 直接删除,不考虑子项处理
     */
    fun delete(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            App.getInstance().db.getItemDao().deleteItem(item)
        }
    }

    /**
     * 递归删除文件夹及文件夹下的子项
     */
    fun deleteFolderAndChildren(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            val dao = App.getInstance().db.getItemDao()
            val deleteList = arrayListOf<Item>()
            val stack = Stack<Item>()
            stack.push(item)
            while (stack.isNotEmpty()) {
                val t = stack.pop()
                val children = dao.getItemByParentId(t.id).first()
                children.forEach {
                    stack.push(it)
                }
                deleteList.add(t)
            }
            dao.deleteItem(deleteList)
            updateList()
        }
    }

    /**
     * 删除item,并将子项的parentId修改为当前打开的文件夹id
     */
    fun deleteOnlyFolder(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = App.getInstance().stack.peek().id
            val dao = App.getInstance().db.getItemDao()
            val list = dao.getItemByParentId(item.id).first()
            val newList = arrayListOf<Item>()
            list.forEach {
                newList.add(it.copy(parentId = id))
            }
            dao.updateItem(newList)
            delete(item)
            updateList()
        }
    }
}
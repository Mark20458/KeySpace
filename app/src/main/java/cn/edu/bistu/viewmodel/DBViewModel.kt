package cn.edu.bistu.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.edu.bistu.App
import cn.edu.bistu.database.model.Item
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

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
}
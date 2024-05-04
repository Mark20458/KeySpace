package cn.edu.bistu.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import cn.edu.bistu.database.model.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("select * from key_table where parentId = :parentId order by max(cTime,coalesce(mTime,0))")
    fun getItemByParentId(parentId: Int): Flow<List<Item>>

    @Query("select * from key_table")
    fun getAll(): Flow<List<Item>>

    @Query(
        "SELECT * FROM key_table " +
                "WHERE name LIKE '%' || :keyword || '%' " +
                "OR username LIKE '%' || :keyword || '%' " +
                "OR websiteName LIKE '%' || :keyword || '%' " +
                "OR note LIKE '%' || :keyword || '%' " +
                "OR url LIKE '%' || :keyword || '%' "
    )
    fun searchByKeyword(keyword: String): Flow<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: List<Item>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: Item)

    @Update
    fun updateItem(item: Item)

    @Update
    fun updateItem(item: List<Item>)

    @Delete
    fun deleteItem(item: Item)

    @Delete
    fun deleteItem(item: List<Item>)


    @Query("delete from key_table")
    fun deleteAll()
}
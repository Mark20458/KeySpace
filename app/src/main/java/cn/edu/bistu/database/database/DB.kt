package cn.edu.bistu.database.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import cn.edu.bistu.database.dao.ItemDao
import cn.edu.bistu.database.model.Item

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class DB : RoomDatabase() {
    abstract fun getItemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: DB? = null

        fun getDatabase(context: Context): DB {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, DB::class.java, "password_db")
                    .build().also { Instance = it }
            }
        }
    }
}
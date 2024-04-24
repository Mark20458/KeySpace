package cn.edu.bistu.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID


/**
 * 用来存储密码，也可以为文件夹
 */
@Entity(tableName = "key_table")
data class Item(
    /**
     * 主键
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /**
     * 密钥或文件夹名称
     */
    val name: String? = null,

    /**
     * 用户名/手机号/邮箱
     */
    val username: String? = null,

    /**
     * 网站名称
     */
    val websiteName: String? = null,

    /**
     * 网址（密钥使用的地方）
     */
    val url: String? = null,

    /**
     * 密码，如果是文件夹则可以为空
     */
    val password: String? = null,

    /**
     * 附注
     */
    val note: String? = null,

    /**
     * 创建时间
     */
    val cTime: Long = System.currentTimeMillis(),

    /**
     * 修改时间
     */
    val mTime: Long? = null,

    /**
     * 唯一id，用于判断云端和本地密码是否相同
     */
    val uuid: String = UUID.randomUUID().toString(),

    /**
     * 父结点的id，-1表示在根结点下面
     */
    val parentId: Int = -1,

    /**
     * 是否为密码
     */
    val isKey: Boolean = false
)
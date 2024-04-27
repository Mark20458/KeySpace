### 密码管理器

- [ ] 密钥管理（增删改查）
- [ ] 进入多任务界面，页面变为空白；防止截图
- [ ] 密码生成
- [ ] 文件夹模式
- [ ] 云端同步（登录、注册、更新）
- [ ] 自动填充服务
- [ ] 搜索功能

#### 1. 数据库设计

##### 手机端Key

| 字段名          | 类型        | 意义                                   |
| :-------------- | :---------- | -------------------------------------- |
| id              | Long        | 主键                                   |
| **name**        | **String?** | **密钥或文件夹名称**                   |
| **username**    | **String?** | **用户名/手机号/邮箱**                 |
| **url**         | **String?** | **网址（密钥使用的地方）**             |
| **websiteName** | **String?** | **网站名称**                           |
| password        | String?     | 密码，如果是文件夹则可以为空           |
| **note**        | **String?** | **附注**                               |
| **cTime**       | **Long**    | **创建时间**                           |
| **mTime**       | **Long?**   | **修改时间**                           |
| uuid            | String      | 唯一id，用于判断云端和本地密码是否相同 |
| parentId        | Int         | 父结点的id，-1表示在根结点下面         |
| isKey           | Boolean     | 是否为密码                             |

> 这里采用的是存储父结点的id表示文件夹，parentId = -1表示该密钥（或文件夹）在根节点下，可以使用这个实现文件夹功能
>
> 加粗字段表示搜索时检索哪些字段

##### SharedPreferencesUtil 
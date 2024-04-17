package cn.edu.bistu.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

class PreferencesKey {
    companion object {
        /**
         * 云登陆状态
         */
        val LOGIN_STATE = booleanPreferencesKey("login_state")

        /**
         * 登录之后会获取一个token，用于和云端进行同步备份
         */
        val TOKEN = stringPreferencesKey("token")

        /**
         * 主密码，只存在于本地，用于加密
         */
        val MASTER_PASSWORD = stringPreferencesKey("master_password")

        /**
         * 进入软件的密码，一般会将密码和盐hash后存起来
         */
        val PASSWORD = stringPreferencesKey("password")

        /**
         * 盐
         */
        val SALT = stringPreferencesKey("salt")

        /**
         * 能否支持生物解锁
         */
        val IS_BIOMETRIC_ENABLE = booleanPreferencesKey("")

        /**
         * 生成密码长度
         */
        val GENERATE_PASSWORD_LENGTH = intPreferencesKey("generate_password_length")

        /**
         * 生成密码是否包含小写字母
         */
        val USE_LOWERCASE_LETTERS = booleanPreferencesKey("use_lowercase_letters")

        /**
         * 生成密码是否包含大写字母
         */
        val USE_UPPERCASE_LETTERS = booleanPreferencesKey("use_uppercase_letters")

        /**
         * 生成密码是否包含数字
         */
        val USE_DIGITS = booleanPreferencesKey("use_digits")

        /**
         * 生成密码是否包含特殊字符
         */
        val USE_SPECIAL_CHARS = booleanPreferencesKey("use_special_chars")
    }
}

val Context.dataStore by preferencesDataStore(
    name = "settings"
)
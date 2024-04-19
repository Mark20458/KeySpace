package cn.edu.bistu.util

import com.tencent.mmkv.MMKV

class PreferencesKey {
    companion object {
        /**
         * 云登陆状态
         */
        const val LOGIN_STATE = "login_state"

        /**
         * 登录之后会获取一个token，用于和云端进行同步备份
         */
        const val TOKEN = "token"

        /**
         * 主密码，只存在于本地，用于加密
         */
        const val MASTER_PASSWORD = "master_password"

        /**
         * 进入软件的密码，一般会将密码和盐hash后存起来
         */
        const val PASSWORD = "password"

        /**
         * 盐
         */
        const val SALT = "salt"

        /**
         * 能否支持生物解锁
         */
        const val IS_BIOMETRIC_ENABLE = "is_biometric_enable"

        /**
         * 生成密码长度
         */
        const val GENERATE_PASSWORD_LENGTH = "generate_password_length"

        /**
         * 生成密码是否包含小写字母
         */
        const val USE_LOWERCASE_LETTERS = "use_lowercase_letters"

        /**
         * 生成密码是否包含大写字母
         */
        const val USE_UPPERCASE_LETTERS = "use_uppercase_letters"

        /**
         * 生成密码是否包含数字
         */
        const val USE_DIGITS = "use_digits"

        /**
         * 生成密码是否包含特殊字符
         */
        const val USE_SPECIAL_CHARS = "use_special_chars"
    }
}

class SPUtil {
    companion object {
        fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
            return MMKV.defaultMMKV().decodeBool(key, defaultValue)
        }

        fun saveBoolean(key: String, value: Boolean) {
            MMKV.defaultMMKV().encode(key, value)
        }

        fun getString(key: String, defaultValue: String? = null): String? {
            return MMKV.defaultMMKV().decodeString(key, defaultValue)
        }

        fun saveString(key: String, value: String) {
            MMKV.defaultMMKV().encode(key, value)
        }
    }
}
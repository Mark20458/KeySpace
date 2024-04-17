package cn.edu.bistu

import androidx.datastore.preferences.core.edit
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import cn.edu.bistu.util.PreferencesKey
import cn.edu.bistu.util.dataStore
import cn.edu.bistu.util.hash
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("cn.edu.bistu", appContext.packageName)
        val password = "123456"
        val salt = "123456"
        runBlocking {
            appContext.dataStore.edit {
                it[PreferencesKey.PASSWORD] = hash(password + salt)
                it[PreferencesKey.SALT] = "123456"
            }
        }
    }
}
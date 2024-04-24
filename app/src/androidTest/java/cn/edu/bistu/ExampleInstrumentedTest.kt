package cn.edu.bistu

import androidx.lifecycle.asLiveData
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import cn.edu.bistu.database.database.DB
import cn.edu.bistu.database.model.Item
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
        val dao = DB.getDatabase(appContext).getItemDao()
        dao.insertItem(Item(name = "aasdioaojsd"))
        val listFlow = dao.getItemByParentId(-1)
    }
}
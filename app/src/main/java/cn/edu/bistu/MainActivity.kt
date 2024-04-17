package cn.edu.bistu

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import cn.edu.bistu.databinding.ActivityMainBinding
import cn.edu.bistu.util.PreferencesKey
import cn.edu.bistu.util.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var mBind: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBind.root)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        runBlocking {
            if (this@MainActivity.dataStore.data.first()[PreferencesKey.LOGIN_STATE] == true) {
                navController.navigate(R.id.action_startFragment_to_lockFragment)
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            mBind.container.visibility = View.VISIBLE
            mBind.blank.visibility = View.GONE
        } else {
            mBind.container.visibility = View.GONE
            mBind.blank.visibility = View.VISIBLE
        }
    }
}
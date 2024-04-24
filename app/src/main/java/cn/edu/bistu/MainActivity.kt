package cn.edu.bistu

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import cn.edu.bistu.databinding.ActivityMainBinding
import cn.edu.bistu.util.PreferencesKey
import cn.edu.bistu.util.SPUtil

class MainActivity : AppCompatActivity() {
    private lateinit var mBind: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBind.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHostFragment.navController
        if (SPUtil.getBoolean(PreferencesKey.LOGIN_STATE)) {
            navController.navigate(R.id.action_startFragment_to_lockFragment)
        }
    }
}
package cn.edu.bistu

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import cn.edu.bistu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mBind: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBind.root)
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
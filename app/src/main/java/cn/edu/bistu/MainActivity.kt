package cn.edu.bistu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.edu.bistu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var mBind: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBind.root)
    }
}
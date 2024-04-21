package cn.edu.bistu.ui.lock

import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import cn.edu.bistu.App
import cn.edu.bistu.R
import cn.edu.bistu.databinding.FragmentLockBinding
import cn.edu.bistu.util.InputMethodUtil
import cn.edu.bistu.util.PreferencesKey
import cn.edu.bistu.util.SPUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LockFragment : Fragment(), View.OnClickListener {
    private lateinit var mBind: FragmentLockBinding

    // 显示的icon数量
    private var iconCount: Int = 1

    //是否显示密码
    private var showPassword: Boolean = false;

    // 取消显示tip
    private var cancelShowTipJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentLockBinding.inflate(inflater)
        initView()
        initListener()
        return mBind.root
    }

    private fun initView() {
        mBind.icon1.visibility = View.GONE
        mBind.separator.visibility = View.GONE
        mBind.icon2.setImageResource(R.drawable.baseline_fingerprint_24)
    }

    private fun initListener() {
        mBind.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isBlank()) {
                    mBind.icon1.visibility = View.GONE
                    mBind.separator.visibility = View.GONE
                    mBind.icon2.visibility = View.VISIBLE
                    mBind.icon2.setImageResource(R.drawable.baseline_fingerprint_24)
                    iconCount = 1
                } else {
                    mBind.icon1.visibility = View.VISIBLE
                    mBind.separator.visibility = View.VISIBLE
                    mBind.icon2.visibility = View.VISIBLE
                    mBind.icon2.setImageResource(if (showPassword) R.drawable.eye else R.drawable.uneye)
                    iconCount = 2
                }
            }
        })
        mBind.icon1.setOnClickListener(this)
        mBind.icon2.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            mBind.icon1.id -> {
                if (SPUtil.getString(PreferencesKey.MASTER_PASSWORD) == mBind.etPassword.text.toString()) {
                    Navigation.findNavController(
                        requireActivity(),
                        R.id.container
                    ).navigate(R.id.action_lockFragment_to_mainFragment)
                } else {
                    lifecycleScope.launch {
                        mBind.tip.text = ""
                        delay(100)
                        mBind.tip.text = "主密码错误，请重新输入！！！"
                        cancelShowTipJob?.cancel()
                        cancelShowTipJob = launch {
                            delay(3000)
                            launch(Dispatchers.Main) {
                                mBind.tip.text = ""
                            }
                        }
                        cancelShowTipJob?.start()
                    }
                    InputMethodUtil.hide(requireActivity(), mBind.input)
                }
            }

            mBind.icon2.id -> {
                if (iconCount == 1) {
                    // 判断是否开启了指纹解锁功能，默认开启该功能
                    if (SPUtil.getBoolean(PreferencesKey.IS_BIOMETRIC_ENABLE, false)) {
                        // 进行指纹解锁
                        useFingerprintUnlock()
                    } else {
                        lifecycleScope.launch {
                            mBind.tip.text = ""
                            delay(100)
                            mBind.tip.text = "tip: 解锁后在设置内启用指纹解锁功能！！！"
                            cancelShowTipJob?.cancel()
                            cancelShowTipJob = launch {
                                delay(3000)
                                launch(Dispatchers.Main) {
                                    mBind.tip.text = ""
                                }
                            }
                            cancelShowTipJob?.start()
                        }
                    }
                } else {
                    // 改变密码是否可见
                    showPassword = !showPassword
                    mBind.icon2.setImageResource(if (showPassword) R.drawable.eye else R.drawable.uneye)
                    if (showPassword) {
                        mBind.etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    } else {
                        mBind.etPassword.inputType =
                            InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
                    }
                }
            }
        }
    }

    private fun useFingerprintUnlock() {
        val isBlank = App.getInstance().isBlank
        val executor = ContextCompat.getMainExecutor(requireActivity())
        val biometricPrompt =
            BiometricPrompt(this,
                executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    // 指纹验证成功
                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {
                        App.getInstance().isBlank = isBlank
                        Navigation.findNavController(
                            requireActivity(),
                            R.id.container
                        ).navigate(R.id.action_lockFragment_to_mainFragment)
                    }

                    override fun onAuthenticationFailed() {
                        App.getInstance().isBlank = isBlank
                        super.onAuthenticationFailed()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        App.getInstance().isBlank = isBlank
                        super.onAuthenticationError(errorCode, errString)
                    }
                }
            )
        val promptInfo = BiometricPrompt.PromptInfo.Builder().setTitle("KeySpace")
            .setNegativeButtonText("取消")
            .build()
        App.getInstance().isBlank = false
        biometricPrompt.authenticate(promptInfo)
    }

}
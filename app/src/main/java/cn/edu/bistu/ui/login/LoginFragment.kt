package cn.edu.bistu.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import cn.edu.bistu.R
import cn.edu.bistu.databinding.FragmentLoginBinding
import cn.edu.bistu.util.InputMethodUtil
import cn.edu.bistu.util.ToastUtil
import cn.edu.bistu.viewmodel.NetworkViewModel


/**
 * TODO 邮箱 密码 确认密码 主密码格式确认
 */
class LoginFragment : Fragment(), View.OnClickListener {
    companion object {
        const val TAG = "login"
        fun newInstance() = LoginFragment()
    }

    private val viewModel: NetworkViewModel by viewModels()
    private lateinit var mBind: FragmentLoginBinding
    private var login: Boolean = false
    private var check_email: Boolean = true
    private var check_password: Boolean = true
    private var check_confirm_password: Boolean = true
    private var check_master_password: Boolean = true

    // 输入格式是否正确
    private val check: Boolean
        get() {
            return check_email && check_password && check_confirm_password && check_master_password
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        login = arguments?.getBoolean("login") ?: login
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = FragmentLoginBinding.inflate(inflater)
        initView()
        initListener()
        initCheck()
        return mBind.root
    }

    private fun initView() {
        mBind.run {
            if (login) {
                tvTitle.text = "登录"
                confirmPasswordLayout.visibility = View.GONE
                check_email = false
                check_password = false
                check_confirm_password = true
                check_master_password = false
            } else {
                tvTitle.text = "注册"
                check_email = false
                check_password = false
                check_confirm_password = false
                check_master_password = false
            }
        }
    }

    private fun initListener() {
        mBind.ivBack.setOnClickListener(this)
        mBind.confirm.setOnClickListener(this)


    }

    private fun initCheck() {
        mBind.email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                val e_mail = s.toString()
                val emailRegex =
                    Regex("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
                if (e_mail.isEmpty() || !emailRegex.matches(e_mail)) {
                    check_email = false
                    mBind.emailLayout.error = "邮箱格式不正确"
                } else {
                    check_email = true
                    mBind.emailLayout.error = null
                }
            }
        })
        mBind.password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.trim().length < 6) {
                    check_password = false
                    mBind.passwordLayout.error = "密码长度需要不少于6位"
                } else {
                    check_password = true
                    mBind.passwordLayout.error = null
                }
            }
        })

        mBind.confirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.trim().length < 6) {
                    check_confirm_password = false
                    mBind.confirmPasswordLayout.error = "密码和确认密码不相同"
                } else {
                    check_confirm_password = true
                    mBind.confirmPasswordLayout.error = null
                }
            }
        })

        mBind.masterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                return
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                return
            }

            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.trim().length < 6) {
                    check_master_password = false
                    mBind.masterPasswordLayout.error = "主密码长度需要不少于6位"
                } else {
                    check_master_password = true
                    mBind.masterPasswordLayout.error = null
                }
            }
        })
    }


    override fun onClick(v: View?) {
        if (v == null) return
        when (v.id) {
            mBind.ivBack.id -> {
                Navigation.findNavController(requireActivity(), R.id.container).popBackStack()
            }

            mBind.confirm.id -> {
                InputMethodUtil.hide(requireContext(), v)
                if (!check) {
                    ToastUtil.show("请根据提示正确填写")
                    return
                } else {
                    val e_mail = mBind.email.text.toString()
                    val password = mBind.password.text.toString()
                    val master_password = mBind.masterPassword.text.toString()
                    if (login) {
                        // 登录
                        viewModel.login(e_mail, password, master_password) {
                            val navController =
                                Navigation.findNavController(requireActivity(), R.id.container)
                            navController.navigate(R.id.action_loginFragment_to_mainFragment)
                        }
                    } else {
                        // 注册
                        viewModel.register(e_mail, password, master_password) {
                            val navController =
                                Navigation.findNavController(requireActivity(), R.id.container)
                            navController.navigate(R.id.action_loginFragment_to_mainFragment)
                        }
                    }
                }
            }
        }
    }
}
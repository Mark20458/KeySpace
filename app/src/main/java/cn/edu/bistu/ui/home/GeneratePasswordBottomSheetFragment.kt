package cn.edu.bistu.ui.home

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.edu.bistu.databinding.BottomSheetFragmentGeneratePasswordBinding
import cn.edu.bistu.util.PreferencesKey
import cn.edu.bistu.util.SPUtil
import cn.edu.bistu.util.generatePassword
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.Slider

class GeneratePasswordBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var mBind: BottomSheetFragmentGeneratePasswordBinding
    private var acceptCallback: ((password: String) -> Unit)? = null
    private var passwordLength: Int = 20
    private var uppercaseLetters: Boolean = true
    private var lowercaseLetters: Boolean = true
    private var digits: Boolean = true
    private var specialChars: Boolean = true

    companion object {
        fun getInstance(): GeneratePasswordBottomSheetFragment {
            return GeneratePasswordBottomSheetFragment()
        }
    }

    fun setAcceptCallback(callback: (password: String) -> Unit): GeneratePasswordBottomSheetFragment {
        acceptCallback = callback
        return this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBind = BottomSheetFragmentGeneratePasswordBinding.inflate(inflater, container, false)
        val layoutParams = mBind.root.layoutParams ?: ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mBind.root.layoutParams = layoutParams
        loadData()
        initView()
        initListener()
        return mBind.root
    }

    private fun loadData() {
        passwordLength = SPUtil.getInt(PreferencesKey.GENERATE_PASSWORD_LENGTH, 18)
        uppercaseLetters = SPUtil.getBoolean(PreferencesKey.USE_UPPERCASE_LETTERS, true)
        lowercaseLetters = SPUtil.getBoolean(PreferencesKey.USE_LOWERCASE_LETTERS, true)
        digits = SPUtil.getBoolean(PreferencesKey.USE_DIGITS, true)
        specialChars = SPUtil.getBoolean(PreferencesKey.USE_SPECIAL_CHARS, true)
    }

    private fun initView() {
        mBind.passwordLength.text = passwordLength.toString()
        mBind.passwordLengthSlider.value = passwordLength.toFloat()
        mBind.lowercaseLetters.isChecked = lowercaseLetters
        mBind.uppercaseLetters.isChecked = uppercaseLetters
        mBind.digits.isChecked = digits
        mBind.specialChars.isChecked = specialChars
        generatePassword()
    }

    private fun initListener() {
        mBind.accept.setOnClickListener {
            acceptCallback?.invoke(mBind.password.text.toString())
            this.dismiss()
        }

        mBind.refuse.setOnClickListener {
            this.dismiss()
        }

        mBind.passwordLengthSlider.addOnChangeListener { _, value, _ ->
            SPUtil.saveInt(PreferencesKey.GENERATE_PASSWORD_LENGTH, value.toInt())
            mBind.passwordLength.text = (value.toInt()).toString()
            passwordLength = value.toInt()
        }

        mBind.passwordLengthSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(p0: Slider) {
            }

            override fun onStopTrackingTouch(p0: Slider) {
                passwordLength = p0.value.toInt()
                SPUtil.saveInt(PreferencesKey.GENERATE_PASSWORD_LENGTH, passwordLength)
                generatePassword()
            }

        })

        mBind.lowercaseLetters.setOnCheckedChangeListener { box, isChecked ->
            lowercaseLetters = isChecked
            SPUtil.saveBoolean(PreferencesKey.USE_LOWERCASE_LETTERS, lowercaseLetters)
            generatePassword()
        }

        mBind.uppercaseLetters.setOnCheckedChangeListener { box, isChecked ->
            uppercaseLetters = isChecked
            SPUtil.saveBoolean(PreferencesKey.USE_UPPERCASE_LETTERS, uppercaseLetters)
            generatePassword()
        }

        mBind.digits.setOnCheckedChangeListener { box, isChecked ->
            digits = isChecked
            SPUtil.saveBoolean(PreferencesKey.USE_DIGITS, digits)
            generatePassword()
        }

        mBind.specialChars.setOnCheckedChangeListener { box, isChecked ->
            specialChars = isChecked
            SPUtil.saveBoolean(PreferencesKey.USE_SPECIAL_CHARS, specialChars)
            generatePassword()
        }

        mBind.passwordLayout.setEndIconOnClickListener {
            generatePassword()
        }
    }

    private fun generatePassword() {
        val password = generatePassword(
            length = passwordLength,
            useUppercaseLetters = uppercaseLetters,
            useLowercaseLetters = lowercaseLetters,
            useDigits = digits,
            useSpecialChars = specialChars
        )
        mBind.password.text = Editable.Factory.getInstance().newEditable(password)
    }
}
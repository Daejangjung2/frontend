package com.example.daejangjung2.feature.auth.signup

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingActivity
import com.example.daejangjung2.databinding.ActivitySignupFirstBinding
import com.example.daejangjung2.feature.auth.login.LoginSecondActivity
import android.graphics.Color
import androidx.core.widget.addTextChangedListener

class SignupFirstActivity : BindingActivity<ActivitySignupFirstBinding>(R.layout.activity_signup_first) {
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        // 모든 입력 필드에 대해 텍스트 변경 리스너를 추가
        binding.etName.addTextChangedListener { checkFieldsForEmptyValues() }
        binding.etEmail.addTextChangedListener { checkFieldsForEmptyValues() }
        binding.etPw.addTextChangedListener { checkFieldsForEmptyValues() }
        binding.etPwCheck.addTextChangedListener { checkFieldsForEmptyValues() }

        binding.btnContinue.setOnClickListener {
            // 버튼이 활성화되어 있을 때만 다음 화면으로 이동
            if (binding.btnContinue.isEnabled) {
                val intent = Intent(this, SignupSecondActivity::class.java)
                startActivity(intent)
            }
        }
    }


    private val passwordPattern = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,16}$")

    private fun isPasswordValid(password: String): Boolean {
        return passwordPattern.matches(password)
    }


    // 모든 필드의 빈칸 여부를 확인하고 버튼 상태를 변경하는 함수
    private fun checkFieldsForEmptyValues() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val pw = binding.etPw.text.toString().trim()
        val pwCheck = binding.etPwCheck.text.toString().trim()

        val allFieldsFilled = name.isNotEmpty() && email.isNotEmpty() && pw.isNotEmpty() && pwCheck.isNotEmpty()

        val isPasswordValid = isPasswordValid(pw)

        // 모든 필드가 비어있지 않으면 버튼 활성화
        if (allFieldsFilled) {
            binding.btnContinue.isEnabled = true
            binding.btnContinue.setBackgroundColor(Color.parseColor("#4682FF")) // 진한 파란색으로 변경
        } else {
            binding.btnContinue.isEnabled = false
        }

        if (!isPasswordValid && pw.isNotEmpty()) {
            binding.etPw.error = "비밀번호는 8~16자, 문자, 숫자, 특수 문자를 포함해야 합니다."
        }
    }
}


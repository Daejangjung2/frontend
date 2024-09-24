package com.example.daejangjung2.feature.auth.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingActivity
import com.example.daejangjung2.databinding.ActivityLoginBinding
import com.example.daejangjung2.databinding.ActivityLoginFourthBinding
import com.example.daejangjung2.feature.main.MainActivity

class LoginFourthActivity : BindingActivity<ActivityLoginFourthBinding>(R.layout.activity_login_fourth ) {
    private val viewModel: LoginViewModel by viewModels { LoginViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        // 모든 입력 필드에 대해 텍스트 변경 리스너를 추가
        binding.etNewPw.addTextChangedListener { checkFieldsForEmptyValues() }
        binding.etReNewPw.addTextChangedListener { checkFieldsForEmptyValues() }

        binding.btnContinue.setOnClickListener {
            // 버튼이 활성화되어 있을 때만 다음 화면으로 이동
            if (binding.btnContinue.isEnabled) {
                val intent = Intent(this, LoginFourthActivity::class.java)
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
        val pw = binding.etNewPw.text.toString().trim()
        val repw = binding.etReNewPw.text.toString().trim()

        val allFieldsFilled = pw.isNotEmpty() && repw.isNotEmpty()

        val isPasswordValid = isPasswordValid(pw)

        // 모든 필드가 비어있지 않으면 버튼 활성화
        if (allFieldsFilled) {
            binding.btnContinue.isEnabled = true
            binding.btnContinue.setBackgroundColor(Color.parseColor("#4682FF")) // 진한 파란색으로 변경
        } else {
            binding.btnContinue.isEnabled = false
        }

        if (!isPasswordValid && pw.isNotEmpty()) {
            binding.etNewPw.error = "비밀번호는 8~16자, 문자, 숫자, 특수 문자를 포함해야 합니다."
        }

        if(viewModel.isLogin.value){
            login()
        }

    }

    private fun login(){
        startActivity(MainActivity.getIntent(this@LoginFourthActivity))
        finish()
    }

    companion object {
        private const val MOVE_MAIN_AFTER_LOGIN = "move_main_after_login_tag"
    }
}
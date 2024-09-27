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
import com.example.daejangjung2.databinding.ActivityLoginSecondBinding
import com.example.daejangjung2.feature.main.MainActivity
import com.example.daejangjung2.feature.auth.login.LoginViewModel

class LoginSecondActivity : BindingActivity<ActivityLoginSecondBinding>(R.layout.activity_login_second ) {
    private val viewModel: LoginViewModel by viewModels ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        // 모든 입력 필드에 대해 텍스트 변경 리스너를 추가
        binding.etCode.addTextChangedListener { checkFieldsForEmptyValues() }

        binding.btnContinue.setOnClickListener {
            // 버튼이 활성화되어 있을 때만 다음 화면으로 이동
            if (binding.btnContinue.isEnabled) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // 모든 필드의 빈칸 여부를 확인하고 버튼 상태를 변경하는 함수
    private fun checkFieldsForEmptyValues() {
        val code = binding.etCode.text.toString().trim()

        val allFieldsFilled = code.isNotEmpty()

        // 모든 필드가 비어있지 않으면 버튼 활성화
        if (allFieldsFilled) {
            binding.btnContinue.isEnabled = true
            binding.btnContinue.setBackgroundColor(Color.parseColor("#4682FF")) // 진한 파란색으로 변경
        } else {
            binding.btnContinue.isEnabled = false
        }


        if(viewModel.isLogin.value){
            login()
        }
    }

    private fun login(){
        startActivity(MainActivity.getIntent(this@LoginSecondActivity))
        finish()
    }

    companion object {
        private const val MOVE_MAIN_AFTER_LOGIN = "move_main_after_login_tag"
    }
}
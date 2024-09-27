package com.example.daejangjung2.feature.auth.signup

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingActivity
import com.example.daejangjung2.databinding.ActivitySignupSecondBinding

class SignupSecondActivity : BindingActivity<ActivitySignupSecondBinding>(R.layout.activity_signup_second ) {
    private val viewModel: SignUpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        // 모든 입력 필드에 대해 텍스트 변경 리스너를 추가
        binding.etNickname.addTextChangedListener { checkFieldsForEmptyValues() }
        binding.etRegion.addTextChangedListener { checkFieldsForEmptyValues() }
        binding.etSex.addTextChangedListener { checkFieldsForEmptyValues() }

        binding.btnContinue.setOnClickListener {
            // 버튼이 활성화되어 있을 때만 다음 화면으로 이동
            if (binding.btnContinue.isEnabled) {
                val intent = Intent(this, SignupThirdActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // 모든 필드의 빈칸 여부를 확인하고 버튼 상태를 변경하는 함수
    private fun checkFieldsForEmptyValues() {
        val nickname = binding.etNickname.text.toString().trim()
        val region = binding.etRegion.text.toString().trim()
        val sex = binding.etSex.text.toString().trim()

        val allFieldsFilled = nickname.isNotEmpty() && region.isNotEmpty() && sex.isNotEmpty()

        // 모든 필드가 비어있지 않으면 버튼 활성화
        if (allFieldsFilled) {
            binding.btnContinue.isEnabled = true
            binding.btnContinue.setBackgroundColor(Color.parseColor("#4682FF")) // 진한 파란색으로 변경
        } else {
            binding.btnContinue.isEnabled = false// 연한 회색으로 변경
        }
    }
}
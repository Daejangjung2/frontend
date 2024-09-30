package com.example.daejangjung2.feature.auth.signup

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingActivity
import com.example.daejangjung2.databinding.ActivitySignupThirdBinding
import com.example.daejangjung2.feature.auth.signup.consent.ConsentActivity

class SignupThirdActivity :
    BindingActivity<ActivitySignupThirdBinding>(R.layout.activity_signup_third) {
    private val viewModel: SignUpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        // 모든 입력 필드에 대해 텍스트 변경 리스너를 추가
        binding.etPhone.addTextChangedListener { checkFieldsForEmptyValues() }
//        binding.etCode.addTextChangedListener { checkFieldsForEmptyValues() }

        binding.btnContinue.setOnClickListener {
            // 버튼이 활성화되어 있을 때만 다음 화면으로 이동
            if (binding.btnContinue.isEnabled) {
                val intent = Intent(this, SignupFourthActivity::class.java)
                startActivity(intent)
            }
        }

        setObserve()
        onCheckBoxHandler()
    }

    private fun setObserve() {
        viewModel.event.observe(this) { handleEvent(it) }
    }

    private fun onCheckBoxHandler() {
        binding.cbConsent1.setOnClickListener { onCheckChanged(binding.cbConsent1) }    // 이용
        binding.cbConsent2.setOnClickListener { onCheckChanged(binding.cbConsent2) }    // 개인
        binding.cbConsent3.setOnClickListener { onCheckChanged(binding.cbConsent3) }    // 전체
    }

    private fun onCheckChanged(compoundButton: CompoundButton) {
        when (compoundButton.id) {
            R.id.cb_consent3 -> {
                if (binding.cbConsent3.isChecked) {
                    binding.cbConsent1.isChecked = true
                    binding.cbConsent2.isChecked = true
                } else {
                    binding.cbConsent1.isChecked = false
                    binding.cbConsent2.isChecked = false
                }
            }

            else -> {
                binding.cbConsent3.isChecked = (
                        binding.cbConsent1.isChecked && binding.cbConsent2.isChecked)
            }
        }
    }

    private fun handleEvent(event: SignUpViewModel.Event) {
        when (event) {
            SignUpViewModel.Event.Success -> {}
            is SignUpViewModel.Event.Consent -> {
                when(event.type){
                    ConsentType.PERSON -> {
                        startActivity(
                            ConsentActivity.getIntent(
                                this@SignupThirdActivity,
                                "개인"
                            )
                        )
                    }
                    ConsentType.PROGRAM -> {
                        startActivity(
                            ConsentActivity.getIntent(
                                this@SignupThirdActivity,
                                "이용"
                            )
                        )
                    }
                }
            }

            is SignUpViewModel.Event.Failed -> {}
        }
    }

    // 모든 필드의 빈칸 여부를 확인하고 버튼 상태를 변경하는 함수
    private fun checkFieldsForEmptyValues() {
        val phone = binding.etPhone.text.toString().trim()
//        val code = binding.etCode.text.toString().trim()

        val allFieldsFilled = phone.isNotEmpty()

        // 모든 필드가 비어있지 않으면 버튼 활성화
        if (allFieldsFilled) {
            binding.btnContinue.isEnabled = true
            binding.btnContinue.setBackgroundColor(Color.parseColor("#4682FF")) // 진한 파란색으로 변경
        } else {
            binding.btnContinue.isEnabled = false
        }
    }

    companion object{
        private const val REQUEST_CODE: Int = 1
    }
}
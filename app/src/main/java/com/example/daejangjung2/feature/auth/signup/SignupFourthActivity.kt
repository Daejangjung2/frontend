package com.example.daejangjung2.feature.auth.signup

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingActivity
import com.example.daejangjung2.databinding.ActivitySignupFourthBinding
import com.example.daejangjung2.feature.auth.login.LoginSecondActivity
import com.example.daejangjung2.feature.main.MainActivity

class SignupFourthActivity : BindingActivity<ActivitySignupFourthBinding>(R.layout.activity_signup_fourth ) {
    private val viewModel: SignUpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        binding.btnContinue.setOnClickListener {
            if (binding.btnContinue.isEnabled) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
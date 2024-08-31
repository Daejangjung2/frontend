package com.example.daejangjung2.feature.auth.signup

import android.os.Bundle
import androidx.activity.viewModels
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingActivity
import com.example.daejangjung2.databinding.ActivitySignupSecondBinding

class SignupSecondActivity : BindingActivity<ActivitySignupSecondBinding>(R.layout.activity_signup_second ) {
    private val viewModel: SignUpViewModel by viewModels { SignUpViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this
    }
}
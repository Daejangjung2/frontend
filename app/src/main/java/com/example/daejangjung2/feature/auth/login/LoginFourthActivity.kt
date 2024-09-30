package com.example.daejangjung2.feature.auth.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingActivity
import com.example.daejangjung2.databinding.ActivityLoginBinding
import com.example.daejangjung2.databinding.ActivityLoginFourthBinding
import com.example.daejangjung2.feature.main.MainActivity

class LoginFourthActivity : BindingActivity<ActivityLoginFourthBinding>(R.layout.activity_login_fourth) {
    private val viewModel: LoginViewModel by viewModels{LoginViewModel.Factory}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        binding.etNewPw.addTextChangedListener {
            viewModel.pwd.value = it.toString().trim()
        }

        binding.etReNewPw.addTextChangedListener {
            viewModel.pwd.value = it.toString().trim()
        }

        viewModel.formState.observe(this) { formState ->
            when (formState) {
                is LoginViewModel.FormState.Correct -> {
                    binding.btnContinue.isEnabled = true
                    binding.btnContinue.setBackgroundColor(ContextCompat.getColor(this, R.color.blue_468FF))
                }
                is LoginViewModel.FormState.Error -> {
                    binding.btnContinue.isEnabled = false
                    Toast.makeText(this, formState.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnContinue.setOnClickListener {
            if (viewModel.formState.value is LoginViewModel.FormState.Correct) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }
        }

        if (viewModel.isLogin.value) {
            login()
        }
    }

    private fun login() {
        startActivity(MainActivity.getIntent(this@LoginFourthActivity))
        finish()
    }

    companion object {
        private const val MOVE_MAIN_AFTER_LOGIN = "move_main_after_login_tag"
    }
}
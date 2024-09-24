package com.example.daejangjung2.feature.auth.login

import android.os.Bundle
import androidx.activity.viewModels
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingActivity
import com.example.daejangjung2.databinding.ActivityLoginBinding
import com.example.daejangjung2.feature.main.MainActivity

class LoginFourthActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login_fourth ) {
    private val viewModel: LoginViewModel by viewModels { LoginViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

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
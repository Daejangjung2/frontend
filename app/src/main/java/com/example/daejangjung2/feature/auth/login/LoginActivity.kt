package com.example.daejangjung2.feature.auth.login

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingActivity
import com.example.daejangjung2.common.view.Toaster
import com.example.daejangjung2.databinding.ActivityLoginBinding
import com.example.daejangjung2.feature.auth.signup.SignupFirstActivity
import com.example.daejangjung2.feature.main.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.util.Utility

class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login ) {
    private val viewModel: LoginViewModel by viewModels { LoginViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this

        //아이디 찾기
        binding.tvFindId.setOnClickListener {
            val intent = Intent(this, LoginFirstActivity::class.java)
            startActivity(intent)
        }

        //비밀번호 찾기
        binding.tvFindPw.setOnClickListener {
            val intent = Intent(this, LoginThirdActivity::class.java)
            startActivity(intent)
        }

        //회원가입
        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, SignupFirstActivity::class.java)
            startActivity(intent)
        }
        setupObserve()
    }

    private fun setupObserve(){
        viewModel.event.observe(this){handleEvent(it)}
        viewModel.authCode.observe(this, Observer { code ->
            if(code != null){
                Log.d("KakaoLogin", "Authorization Code: $code")
            } else {
                Log.e("KakaoLogin", "Failed")
            }
        })
    }

    private fun handleEvent(event: LoginViewModel.Event) {
        when (event) {
            LoginViewModel.Event.InputBlank -> {
                Toaster.showShort(this@LoginActivity, "로그인과 비밀번호가 비어 있습니다.")
            }
            is LoginViewModel.Event.LoginFailed -> {
                Toaster.showShort(this@LoginActivity, "로그인이 실패 하였습니다.")
            }
            LoginViewModel.Event.LoginSuccess -> {
                startActivity(MainActivity.getIntent(this@LoginActivity))
                finish()
            }
            LoginViewModel.Event.KakaoLoginSuccess -> {
                startActivity(MainActivity.getIntent(this@LoginActivity))
                finish()
            }
        }
    }


    companion object {
        private const val MOVE_MAIN_AFTER_LOGIN = "move_main_after_login_tag"
        fun getIntent(context: Context, moveToMain: Boolean = false): Intent {
            return Intent(context, LoginActivity::class.java).apply {
                putExtra(MOVE_MAIN_AFTER_LOGIN, moveToMain)
            }
        }
    }
}
package com.example.daejangjung2.feature.auth.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.daejangjung2.BuildConfig
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingActivity
import com.example.daejangjung2.databinding.ActivityLoginBinding
import com.example.daejangjung2.feature.main.MainActivity

class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login ) {
    private val viewModel: LoginViewModel by viewModels { LoginViewModel.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this
        binding.btnKakaoLogin.setOnClickListener{
            val clientId = BuildConfig.TEST_ID
            val redirectUri = "${BuildConfig.SERVER_URL}api/kakao/auth/login/oauth2"//"https://djj2.site/oauth" //"kakao${BuildConfig.NATIVE_APP_KEY}://oauth"
            val authUrl = "https://kauth.kakao.com/oauth/authorize?client_id=$clientId&redirect_uri=$redirectUri&response_type=code"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
            startActivity(intent)
//            viewModel.loginWithKakao(context = this,redirectUri);
        }

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

        if(viewModel.isLogin.value){

        }
        setupObserve()
    }
    override fun onNewIntent(intent: Intent){
        super.onNewIntent(intent)
        handleIntent(intent)
    }
    private fun handleIntent(intent: Intent){
        intent.data?.let { uri ->
            val url = uri.toString()
            Log.d("KakaoLogin", "Received URL: $url")
            if(url.startsWith("kakao${BuildConfig.NATIVE_APP_KEY}://oauth")) {
                viewModel.handleRedirectedUrl(url)
                Log.d("KakaoLogin", "Handling redirected URL")
            } else {
                Log.e("KakaoLogin", "Unexpected URL, cannot handle")
            }
        }
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
            LoginViewModel.Event.InputBlank -> TODO()
            is LoginViewModel.Event.LoginFailed -> TODO()
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
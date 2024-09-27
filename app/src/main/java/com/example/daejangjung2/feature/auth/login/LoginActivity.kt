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


        var keyHash = Utility.getKeyHash(this)
        Log.d("keyHash", keyHash)
//        binding.btnKakaoLogin.setOnClickListener{
////            val clientId = BuildConfig.TEST_ID
////            val redirectUri = "${BuildConfig.SERVER_URL}api/kakao/auth/login/oauth2"
////            val authUrl = "https://kauth.kakao.com/oauth/authorize?client_id=$clientId&redirect_uri=$redirectUri&response_type=code"
////            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
//////            intent.setPackage("com.android.chrome")
////            startActivity(intent)
//            loginByKakao()
//        }

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

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
        }
    }


//    private fun loginByKakao() {
//        Log.d("loginByKakao", "hi")
//        // 카카오톡 설치 확인
//        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
//            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
//                if (error != null) {
//                    Log.e(TAG, "카카오톡으로 로그인 실패", error)
//
//                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우
//                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
//                        return@loginWithKakaoTalk
//                    }
//
//                    // 카카오톡에 연결된 카카오 계정이 없는 경우, 카카오계정으로 로그인 시도
//                    UserApiClient.instance.loginWithKakaoAccount(this) { accountToken, accountError ->
//                        if (accountError != null) {
//                            Log.e(TAG, "카카오 계정으로 로그인 실패", accountError)
//                        } else if (accountToken != null) {
//                            Log.i(TAG, "카카오 계정으로 로그인 성공 ${accountToken.accessToken}")
//                            // 백엔드로 액세스 토큰 전달
//                            sendAccessTokenToBackend(accountToken.accessToken)
//                        }
//                    }
//                } else if (token != null) {
//                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
//
//                    // 백엔드로 액세스 토큰 전달
//                    sendAccessTokenToBackend(token.accessToken)
//                }
//            }
//        } else {
//            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
//                if (error != null) {
//                    Log.e(TAG, "카카오 계정으로 로그인 실패", error)
//                } else if (token != null) {
//                    Log.i(TAG, "카카오 계정으로 로그인 성공 ${token.accessToken}")
//                    // 백엔드로 액세스 토큰 전달
//                    sendAccessTokenToBackend(token.accessToken)
//                }
//            }
//        }
//    }

//    // 액세스 토큰을 백엔드로 전송하고 JWT 토큰을 수신하는 함수
//    private fun sendAccessTokenToBackend(accessToken: String) {
//        val apiService = RetrofitInstance.apiService // Retrofit 설정된 API 서비스
//
//        apiService.loginWithKakao(accessToken).enqueue(object : Callback<KakaoLoginResponse> {
//            override fun onResponse(call: Call<KakaoLoginResponse>, response: Response<KakaoLoginResponse>) {
//                if (response.isSuccessful) {
//                    val jwtToken = response.body()?.jwtToken
//                    if (jwtToken != null) {
//                        // JWT 토큰을 저장
//                        saveJwtToken(jwtToken)
//
//                        // 메인 화면으로 이동
//                        moveToMainActivity()
//                    }
//                } else {
//                    Log.e(TAG, "JWT 토큰 수신 실패")
//                }
//            }
//
//            override fun onFailure(call: Call<KakaoLoginResponse>, t: Throwable) {
//                Log.e(TAG, "서버 통신 오류", t)
//            }
//        })
//    }

    // JWT 토큰 저장 (예시: SharedPreferences 사용)
    private fun saveJwtToken(jwtToken: String) {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("jwt_token", jwtToken)
            apply()
        }
    }

    // 메인 화면으로 이동하는 함수
    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // 현재 액티비티 종료
    }



//    private fun loginByKakaoTalk() {
//        Log.d("loginByKakaoTalk","hi")
//        UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
//            if (error != null) {
//                if (isClientCancelError(error)) {
//                    logLoginError(error)
//                    return@loginWithKakaoTalk
//                }
//                loginByKakaoAccount()
//            } else if (token != null) {
//                Log.d("token",token.toString())
////                completeLoginByKakao(token)
//
//            }
//        }
//    }
//    private fun isClientCancelError(error: Throwable?) =
//        error is ClientError && error.reason == ClientErrorCause.Cancelled
//    private fun logLoginError(error: Throwable) {
//        Log.d("test", "로그인 실패 $error")
//    }
////    private fun completeLoginByKakao(token: OAuthToken) {
////        viewModel.completeLoginByKakao(token.accessToken)
////    }
//
//    private fun loginByKakaoAccount() {
//    Log.d("loginByKakaoAccount","hi")
//        UserApiClient.instance.loginWithKakaoAccount(this,) { token, error ->
//            Log.d("loginByKakaoAccount","hi")
//            if (error != null) {
//                logLoginError(error)
//            } else if (token != null) {
//                Log.d("token",token.toString())
////                completeLoginByKakao(token)
//            }
//        }
//    }



    override fun onNewIntent(intent: Intent){
        super.onNewIntent(intent)
        Log.d("onNewIntent","success")
        viewModel.handleIntent(this,intent)
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
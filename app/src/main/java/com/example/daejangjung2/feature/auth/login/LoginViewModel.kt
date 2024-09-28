package com.example.daejangjung2.feature.auth.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.daejangjung2.BuildConfig
import com.example.daejangjung2.app.DaejangjungApplication
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.repository.AuthRepository
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    val email: MutableStateFlow<String> = MutableStateFlow("123")
    val pwd: MutableStateFlow<String> = MutableStateFlow("123")
    val code: MutableStateFlow<String> = MutableStateFlow("")
    private lateinit var kakaoAuthService: KakaoAuthService
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> get() = _loginResult
    private val _authCode = MutableLiveData<String?>()
    val authCode: MutableLiveData<String?> = _authCode

    private val _isLogin: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLogin: StateFlow<Boolean>
        get() = _isLogin

    // 이메일 정규식
    private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

    // 비밀번호 정규식 (8~16자, 문자, 숫자, 특수 문자를 포함)
    private val passwordPattern = Regex("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,16}$")

    // 폼 상태 (이메일 또는 비밀번호에 따라 Correct 또는 Error)
    private val _formState: MutableStateFlow<FormState> = MutableStateFlow(FormState.Error("이메일 또는 비밀번호를 입력하세요."))
    val formState: StateFlow<FormState>
        get() = _formState


    init {
        viewModelScope.launch {
            _isLogin.value = authRepository.isLogin
        }

        Log.d("test", authRepository.getToken().accessToken)

        // 이메일 유효성 검사
        viewModelScope.launch {
            email.collect { validateEmail() }
        }

        // 인증번호 유효성 검사
        viewModelScope.launch {
            code.collect { validateVerificationCode() }
        }

        // 비밀번호 유효성 검사
        viewModelScope.launch {
            pwd.collect { validatePassword() }
        }

    }

    // 이메일 유효성 검사
    private fun validateEmail() {
        val isEmailValid = email.value.isNotBlank() && emailRegex.matches(email.value)

        if (isEmailValid) {
            _formState.value = FormState.Correct
        } else {
            _formState.value = FormState.Error("유효하지 않은 이메일 형식입니다.")
        }
    }

    // 인증번호 유효성 검사
    private fun validateVerificationCode() {
        val isCodeFilled = code.value.isNotBlank()

        if (isCodeFilled) {
            _formState.value = FormState.Correct
        } else {
            _formState.value = FormState.Error("인증번호를 입력하세요.")
        }
    }

    // 비밀번호 유효성 검사
    private fun validatePassword() {
        val isPasswordValid = pwd.value.isNotBlank() && passwordPattern.matches(pwd.value)

        if (isPasswordValid) {
            _formState.value = FormState.Correct
        } else if (pwd.value.isNotBlank() && !passwordPattern.matches(pwd.value)) {
            _formState.value = FormState.Error("비밀번호는 8~16자, 문자, 숫자, 특수 문자를 포함해야 합니다.")
        } else {
            _formState.value = FormState.Error("비밀번호를 입력하세요.")
        }
    }
    fun handleIntent(context: Context, intent: Intent){
        intent.data?.let { uri ->
            if (uri.scheme == "kakao${BuildConfig.NATIVE_APP_KEY}" && uri.host == "oauth2") {
                // URI에 포함된 인증 코드 추출
                val accessToken = uri.getQueryParameter("accessToken")
                val refreshToken = uri.getQueryParameter("refreshToken")
                if (accessToken != null) {
                    // 인증 코드 사용하여 서버로 토큰 요청 등 처리
                    Toast.makeText(context, "Authorization Code: $accessToken", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(context, "Authorization code missing", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }




        fun loginWithKakao(context: Context, redirectUri: String) {
//        requestAuthorizationCode(context,BuildConfig.NATIVE_APP_KEY, redirectUri)

//            if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
//                // 카카오톡 로그인 시도
//                UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
//                    if (error != null) {
//                        Log.i("login","fail")
//                        Log.e("KakaoLogin", "카카오 로그인 실패", error)
//                        // 로그인 실패
//                        _loginResult.postValue(false)
//                    } else if (token != null) {
//                        Log.i("login","success")
//                        Log.i("KakaoLogin", "카카오 로그인 성공 ${token.accessToken}")
//                        // 로그인 성공
//                        _loginResult.postValue(true)
//                    }
//                }
//            } else {
//                // 카카오 계정 로그인 시도
//                UserApiClient.instance.loginWithKakaoAccount(context) { token, error ->
//                    if (error != null) {
//                        _loginResult.postValue(false)
//                        Log.e("KakaoLogin", "카카오 로그인 실패", error)
//                    } else if (token != null) {
//                        _loginResult.postValue(true)
//                        Log.i("KakaoLogin", "카카오 로그인 성공 ${token.accessToken}")
//                    }
//                }
//            }

        // WebView 설정 (사용자 로그인 유도)
//        val webView = findViewById<WebView>(R.id.webView)
//        webView.settings.javaScriptEnabled = true
//
//        webView.webViewClient = object : WebViewClient() {
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//                // 인가 코드가 redirect_uri로 전달되는 것을 확인
//                if (url != null && url.startsWith("YOUR_REDIRECT_URI")) {
//                    val uri = Uri.parse(url)
//                    val authCode = uri.getQueryParameter("code")
//                    Log.d("KakaoLogin", "Authorization Code: $authCode")
//                    // 받은 인가 코드를 사용하여 액세스 토큰 요청 등의 후속 작업을 진행
//                }
//            }
//        }
    }
//    private fun requestAuthorizationCode(context: Context, clientId: String, redirectUri: String) {
//        kakaoAuthRepository.fetchAuthorizationCode(
//            clientId = clientId,  // Kakao Developers에서 발급된 Client ID
//            redirectUri = redirectUri,  // Kakao Developers에서 설정한 Redirect URI
//            onSuccess = { authorizationCode ->
//                // 성공적으로 Authorization Code를 받은 경우
//                Toast.makeText(context, "Authorization Code: $authorizationCode", Toast.LENGTH_SHORT).show()
//                Log.i("code", authorizationCode)
//                // 이후 Authorization Code를 사용하여 토큰을 요청하는 등의 로직 처리
//            },
//            onFailure = { throwable ->
//                // 에러 발생 시
//                Toast.makeText(context, "Failed to get authorization code: ${throwable.message}", Toast.LENGTH_SHORT).show()
//            }
//        )
//    }


    fun login() {
        synchronized(this) {
            if (_isLoading.value) return
            _isLoading.value = true
        }
        val email = email.value.trim()
        val pwd = pwd.value.trim()
        viewModelScope.launch {
            if (email.isBlank() || pwd.isBlank()) {
                _event.emit(Event.InputBlank)
                _isLoading.value = false
                return@launch
            }

            when (val response = authRepository.login(email, pwd)) {
                is ApiResponse.Success -> {
                    _event.emit(Event.LoginSuccess)
                }

                is ApiResponse.Failure -> {
                    _event.emit(
                        Event.LoginFailed(
                            try {
                                response.error.toString()
                            }
                            catch (e: Exception){
                                response.error?.firstOrNull().toString()
                            }
                        ),
                    )
                }

                else -> {
                    Event.LoginFailed("알 수 없는 에러가 발생했습니다.")
                }
            }
            _isLoading.value = false
        }
    }

    sealed interface FormState {
        object Correct : FormState
        data class Error(val message: String) : FormState
    }

    sealed interface Event{
        data class LoginFailed(val message: String): Event
        data object LoginSuccess: Event
        data object InputBlank : Event
        data object KakaoLoginSuccess : Event
    }
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return LoginViewModel(
                    DaejangjungApplication.container.authRepository,
                ) as T
            }
        }
    }
}
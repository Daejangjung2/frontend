package com.example.daejangjung2.feature.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.daejangjung2.app.DaejangjungApplication
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean>
        get() = _isLoading

    val id: MutableStateFlow<String> = MutableStateFlow("123")
    val pwd: MutableStateFlow<String> = MutableStateFlow("123")

    private val _isLogin: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLogin: StateFlow<Boolean>
        get() = _isLogin

    init{
        viewModelScope.launch {
            _isLogin.value = authRepository.isLogin
        }

        Log.d("test", authRepository.getToken().accessToken)
    }

    fun login() {
        synchronized(this) {
            if (_isLoading.value) return
            _isLoading.value = true
        }
        val id = id.value.trim()
        val pwd = pwd.value.trim()
        viewModelScope.launch {
            if (id.isBlank() || pwd.isBlank()) {
                _event.emit(Event.InputBlank)
                _isLoading.value = false
                return@launch
            }

            when (val response = authRepository.login("test1234@naver.com", "test1234")) {
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

    sealed interface Event{
        data class LoginFailed(val message: String): Event
        data object LoginSuccess: Event
        data object InputBlank : Event
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
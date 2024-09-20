package com.example.daejangjung2.feature.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.daejangjung2.app.DaejangjungApplication
import com.example.daejangjung2.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SplashViewModel(private val authRepository: AuthRepository): ViewModel() {
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event.asSharedFlow()

    init {
        checkLogin();
    }

    private fun checkLogin(){
        viewModelScope.launch {
            delay(2000)
            val isLogin = authRepository.isLogin
            if(!isLogin){
                _event.emit(Event.NavigateToMain)
            }
            else{
                _event.emit(Event.NavigateToLogin)
            }
        }
    }

    sealed class Event{
        data object NavigateToMain: Event()
        data object NavigateToLogin: Event()
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return SplashViewModel(
                    DaejangjungApplication.container.authRepository,
                ) as T
            }
        }
    }
}
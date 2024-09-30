package com.example.daejangjung2.feature.main.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.daejangjung2.app.DaejangjungApplication
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.repository.AuthRepository
import com.example.daejangjung2.feature.auth.login.LoginViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MyPageViewModel(
    private val authRepository: AuthRepository
): ViewModel() {
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event.asSharedFlow()

    fun logout(){
        viewModelScope.launch {
            when(val response = authRepository.logout()){
                is ApiResponse.Success -> {
                    val message = response.body.message
                    if(!message.isNullOrEmpty()){
                        _event.emit(Event.Logout(message))
                    }else{
                        _event.emit(Event.Logout("로그아웃이 완료되었습니다."))
                    }
                }
                is ApiResponse.Failure -> {
                    _event.emit(Event.Failed("로그아웃을 실패하였습니다.\n재시도 부탁드립니다."))
                }
                else->{

                }
            }
        }
    }

    sealed interface Event{
        data object Success: Event
        data class Failed(val message: String): Event
        data class Logout(val message: String): Event
    }

    companion object{

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return MyPageViewModel(
                    DaejangjungApplication.container.authRepository,
                ) as T
            }
        }
    }
}
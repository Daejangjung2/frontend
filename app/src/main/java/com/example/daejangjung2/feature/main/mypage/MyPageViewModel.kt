package com.example.daejangjung2.feature.main.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.daejangjung2.app.DaejangjungApplication
import com.example.daejangjung2.data.model.response.ProfileResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.repository.MyPageRepository
import com.example.daejangjung2.feature.main.map.MapViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyPageViewModel(
    private val myPageRepository: MyPageRepository
): ViewModel() {
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event.asSharedFlow()

    private val _profile: MutableStateFlow<ProfileResponse> = MutableStateFlow(ProfileResponse.EMPTY_PROFILE)
    val profile: StateFlow<ProfileResponse>
        get() = _profile

    fun myProfile(){
        viewModelScope.launch {
            when(val response = myPageRepository.profile()){
                is ApiResponse.Success -> {
                    response.body?.let { data->
                        _profile.update { data.data }
                    }
                }
                is ApiResponse.Failure -> {

                }
                else ->{

                }
            }
        }
    }


    sealed class Event{
        object Success: Event()
        data class Failed(val message: String): Event()
    }

    companion object{
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return MyPageViewModel(
                    DaejangjungApplication.container.myPageRepository,
                ) as T
            }
        }
    }
}
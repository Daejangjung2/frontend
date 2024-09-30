package com.example.daejangjung2.feature.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SignUpViewModel: ViewModel() {
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event

    // 약관 이용 동의서
    fun usingProgramConsent(){
        viewModelScope.launch {
            _event.emit(Event.Consent(ConsentType.PROGRAM))
        }
    }

    // 개인 정보 동의서
    fun usingPersonConsent(){
        viewModelScope.launch {
            _event.emit(Event.Consent(ConsentType.PERSON))
        }
    }
    sealed interface Event{
        data class Consent(val type: ConsentType): Event
        data object Success: Event
        data class Failed(val message: String): Event
    }
}
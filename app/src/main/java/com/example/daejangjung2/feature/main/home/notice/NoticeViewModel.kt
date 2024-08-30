package com.example.daejangjung2.feature.main.home.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daejangjung2.domain.model.Notice
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoticeViewModel: ViewModel() {
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event.asSharedFlow()

    private val _notice: MutableStateFlow<Notice> = MutableStateFlow(Notice.EMPTY_NOTICE)

    val notice: StateFlow<Notice>
        get() = _notice

    fun getNotice(item: Notice){
        viewModelScope.launch {
            _notice.update {
                item
            }
        }
    }

    sealed class Event{
        data object Success: Event()
        data class Failed(var message: String): Event()
    }
}
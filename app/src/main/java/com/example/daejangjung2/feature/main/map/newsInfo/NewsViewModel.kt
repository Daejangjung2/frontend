package com.example.daejangjung2.feature.main.map.newsInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daejangjung2.R
import com.example.daejangjung2.common.livedata.SingleLiveEvent
import com.example.daejangjung2.domain.model.NewsInfo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewsViewModel:ViewModel() {

    private val _event: SingleLiveEvent<Event> = SingleLiveEvent()
    val event: LiveData<Event>
        get() = _event

    private val _news: SingleLiveEvent<List<NewsInfo>> = SingleLiveEvent()
    val news: LiveData<List<NewsInfo>>
        get() = _news

    sealed class Event{
        data object Success : Event();
        data class Failed(val message: String) : Event();
    }

    fun getTestNews(){
        viewModelScope.launch {
            val newData = listOf(
                NewsInfo("1","송실로 도로 통제", "내용", R.drawable.sample_image),
                NewsInfo("2","뉴스 내용", "내용", R.drawable.sample_image),
                NewsInfo("3","동작산 산사태 발생", "내용", R.drawable.sample_image),
                NewsInfo("4","위급사항", "내용", R.drawable.sample_image)
                // 더 많은 뉴스 항목 추가 가능
            )

            _news.value = newData
        }
    }
}
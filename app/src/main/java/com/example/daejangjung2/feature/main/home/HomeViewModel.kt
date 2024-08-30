package com.example.daejangjung2.feature.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daejangjung2.domain.model.ImageBanner
import com.example.daejangjung2.domain.model.Medal
import com.example.daejangjung2.domain.model.Notice
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event.asSharedFlow()

    private val _banner: MutableStateFlow<ArrayList<ImageBanner>> = MutableStateFlow(ArrayList())
    val banner: StateFlow<ArrayList<ImageBanner>>
        get() = _banner

    private val _medal: MutableStateFlow<ArrayList<Medal>> = MutableStateFlow(ArrayList())
    val medal: StateFlow<ArrayList<Medal>>
        get() = _medal

    private val _notice: MutableStateFlow<List<Notice>> = MutableStateFlow(emptyList())
    val notice: StateFlow<List<Notice>>
        get() = _notice

    val distance: MutableStateFlow<String> = MutableStateFlow("0 M");

    val medalCount: MutableStateFlow<String> = MutableStateFlow("0개");

    fun noticeService(notice: Notice) {
        viewModelScope.launch {
            _event.emit(Event.SelectNotice(notice))
        }
    }



    sealed class Event {
        data class SelectNotice(val notice: Notice): Event();
        data object Success : Event();
        data class Failed(val message: String) : Event();
    }


    fun getTestBanner() {
        viewModelScope.launch {
            val newBanners = arrayListOf(
                ImageBanner(image = "https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_aa0c3b93-1abc-4592-8744-2c2e0e039cd4/web_assets/BAC/assets/images/frame-58.png"),
                ImageBanner(image = "https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_aa0c3b93-1abc-4592-8744-2c2e0e039cd4/web_assets/BAC/assets/images/frame-58_3.png"),
                ImageBanner(image = "https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_aa0c3b93-1abc-4592-8744-2c2e0e039cd4/web_assets/BAC/assets/images/frame-58_5.png"),
                ImageBanner(image = "https://ssproxy.ucloudbiz.olleh.com/v1/AUTH_aa0c3b93-1abc-4592-8744-2c2e0e039cd4/web_assets/BAC/assets/images/frame-58_2.png")
            )
            _banner.update {
                newBanners
            }
        }
    }

    fun getTestMedal() {
        viewModelScope.launch {
            val newMedal = arrayListOf(
                Medal(image = "", comment = "제주자전거길"),
                Medal(image = "", comment = "섬진강자전거길"),
                Medal(image = "", comment = "국토종주\n낙동강자전거길"),
                Medal(image = "", comment = "아라자전거길")
            )
            val newCount = newMedal.size;
            medalCount.update {
                newCount.toString() + "개"
            }
            _medal.update {
                newMedal
            }
        }
    }

    fun getTestNotice() {
        viewModelScope.launch {
            val newNotice = arrayListOf(
                Notice(
                    image = "https://baccenter.blackyak.com/attach/Class/%EC%82%B0%EC%98%A4%EB%A5%B4%EA%B3%A0%20%EC%B2%B4%EB%A0%A5%EC%98%AC%EB%A6%AC%EA%B3%A0%20%EC%8D%B8%EB%84%A4%EC%9D%BC(10).png",
                    comment = "산 오르고, 체력 올리고"
                ),
                Notice(
                    image = "https://baccenter.blackyak.com/attach/Class/%EC%96%B4%EC%9A%B8%EB%A6%BC(1).png",
                    comment = "BAC아카데미 어울림 캠프(일자조율)"
                ),
                Notice(
                    image = "https://baccenter.blackyak.com/attach/Class/%ED%8A%B8%EB%A0%88%EC%9D%BC%EB%9F%AC%EB%8B%9D%2020230125%201(5).jpg",
                    comment = "BAC 트레일러닝스쿨(매월 둘째주 토요일)"
                ),
                Notice(
                    image = "https://baccenter.blackyak.com/attach/Class/GPS%20APP1(1).jpg",
                    comment = "등산기록도 스마트하게 [GPS APP 마스터]"
                ),
                Notice(
                    image = "https://baccenter.blackyak.com/attach/Class/%EB%B0%B1%EC%9A%B4%EB%8C%80%EC%9D%BC%EC%B6%9C%2020230125%202(16).jpg",
                    comment = "북한산 백운대 일출산행"
                )
            )

            _notice.update {
                newNotice
            }
        }
    }
}
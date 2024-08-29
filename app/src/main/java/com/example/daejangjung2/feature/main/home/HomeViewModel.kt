package com.example.daejangjung2.feature.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.daejangjung2.domain.model.ImageBanner
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event.asSharedFlow()

    private val _banner: MutableStateFlow<ArrayList<ImageBanner>> = MutableStateFlow(ArrayList())
    val banner: StateFlow<ArrayList<ImageBanner>>
        get() = _banner

    val distance: MutableStateFlow<String> = MutableStateFlow("0 M");

    val medal: MutableStateFlow<String> = MutableStateFlow("0개");


    fun getBanner(){
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


    sealed class Event {
        object Success: Event();
        data class Failed(val message: String): Event();
    }
}
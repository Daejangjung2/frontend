package com.example.daejangjung2.feature.main.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.daejangjung2.app.DaejangjungApplication
import com.example.daejangjung2.common.livedata.SingleLiveEvent
import com.example.daejangjung2.data.model.response.WeatherResponse
import com.example.daejangjung2.data.model.response.WeatherResponse.Companion.EMPTY_WEATHER
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.GeoPoint
import com.example.daejangjung2.domain.repository.MapRepository
import com.example.daejangjung2.feature.main.map.newsInfo.NewsViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapViewModel(
    private val mapRepository: MapRepository
): ViewModel() {
    private val _event: MutableSharedFlow<Event> = MutableSharedFlow()
    val event: SharedFlow<Event>
        get() = _event.asSharedFlow()

    private var updateCurPositionLoading: Boolean = false

    private val _lastUserPoint: SingleLiveEvent<GeoPoint> = SingleLiveEvent()
    val lastUserPoint: LiveData<GeoPoint>
        get() = _lastUserPoint

    private var visibleMapMinQuarterDistance: Int = 100
    private val _curCameraCenterPoint: SingleLiveEvent<GeoPoint> = SingleLiveEvent()
    val curCameraCenterPoint: LiveData<GeoPoint>
        get() = _curCameraCenterPoint

    private val _weather: MutableStateFlow<WeatherResponse> = MutableStateFlow(EMPTY_WEATHER)
    val weather: StateFlow<WeatherResponse>
        get() = _weather


    // 카카오맵 줌 이벤트 리스너가 제대로 안먹힘. 먹혔다가 안먹혔다가 해서 일단 이건 고정으로 놓을 예정
    fun setVisibleMapDistance(bottomLeft: GeoPoint, topRight: GeoPoint) {
        val widthDistance = DistanceManager.getDistance(
            bottomLeft.latitude,
            bottomLeft.longitude,
            bottomLeft.latitude,
            topRight.longitude,
        )
        val heightDistance = DistanceManager.getDistance(
            bottomLeft.latitude,
            bottomLeft.longitude,
            topRight.latitude,
            bottomLeft.longitude,
        )
        visibleMapMinQuarterDistance = (listOf(widthDistance, heightDistance).min() / 8)
    }

    fun updateCurPosition(geoPoint: GeoPoint) {
        if (updateCurPositionLoading) return
        updateCurPositionLoading = true
        if (_lastUserPoint.value == null) {
            _curCameraCenterPoint.value = geoPoint
            _lastUserPoint.value = geoPoint
            updateCurPositionLoading = false
            return
        }
        _curCameraCenterPoint.value?.let { cameraCenterPoint ->
            val distanceFromCameraCenter = DistanceManager.getDistance(
                cameraCenterPoint.latitude,
                cameraCenterPoint.longitude,
                geoPoint.latitude,
                geoPoint.longitude,
            )
            if (distanceFromCameraCenter > visibleMapMinQuarterDistance) {
                _curCameraCenterPoint.value = geoPoint
            }
            _lastUserPoint.value = geoPoint
        }
        updateCurPositionLoading = false
    }

    fun guideService(){
        viewModelScope.launch {
            _event.emit(Event.Service(ServiceType.GUIDE))
        }
    }

    fun weatherService(){
        viewModelScope.launch {
            when(val response = mapRepository.weather("서울특별시","동작구")){
                is ApiResponse.Success -> {
                    response.body?.let {data ->
                        _weather.update { data.data }
                    }
                    _event.emit(Event.Service(ServiceType.WEATHER))
                }
                is ApiResponse.Failure -> {
                    Log.d("FAIL","실패")
                }
                is ApiResponse.NetworkError -> {
                    Log.d("NETWORK","네트워크")
                }
                is ApiResponse.Unexpected -> {
                    Log.d("Unex","알수없음")
                }
            }
        }
    }

    fun newsService(){
        viewModelScope.launch {
            _event.emit(Event.Service(ServiceType.NEWS))
        }
    }

    fun contentService(){
        viewModelScope.launch {
            _event.emit(Event.Service(ServiceType.CONTENT))
        }
    }

    sealed class Event {
        object Success: Event();
        data class Failed(val message: String): Event();

        data class Service(val service: ServiceType): Event();
    }


    companion object{
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras,
            ): T {
                return MapViewModel(
                    DaejangjungApplication.container.mapRepository,
                ) as T
            }
        }
    }
}
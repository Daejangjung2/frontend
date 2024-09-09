package com.example.daejangjung2.feature.main.map

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.daejangjung2.common.livedata.SingleLiveEvent
import com.example.daejangjung2.domain.model.GeoPoint

class MapViewModel: ViewModel() {
    private val _event: SingleLiveEvent<Event> = SingleLiveEvent()
    val event: LiveData<Event>
        get() = _event

    private var updateCurPositionLoading: Boolean = false

    private val _lastUserPoint: SingleLiveEvent<GeoPoint> = SingleLiveEvent()
    val lastUserPoint: LiveData<GeoPoint>
        get() = _lastUserPoint

    private var visibleMapMinQuarterDistance: Int = 100
    private val _curCameraCenterPoint: SingleLiveEvent<GeoPoint> = SingleLiveEvent()
    val curCameraCenterPoint: LiveData<GeoPoint>
        get() = _curCameraCenterPoint


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

    sealed class Event {
        object Success: Event();
        data class Failed(val message: String): Event();
    }
}
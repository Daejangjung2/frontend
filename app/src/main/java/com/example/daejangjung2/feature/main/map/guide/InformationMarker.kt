package com.example.daejangjung2.feature.main.map.guide

import android.graphics.Color
import android.util.Log
import com.example.daejangjung2.domain.model.GeoPoint
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.route.RouteLineLayer
import com.kakao.vectormap.route.RouteLineManager
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet

class InformationMarker {
    private lateinit var routeLineManager: RouteLineManager // RouteLineManager 추가
    private lateinit var routeLayer: RouteLineLayer
    private lateinit var stylesSet: RouteLineStylesSet
    private lateinit var routeOptions: RouteLineOptions
    private lateinit var routeSegment: RouteLineSegment
    fun setupRouterLine(kakaoMap: KakaoMap, geoPoint: GeoPoint) {
        val lat = geoPoint.latitude
        val lon = geoPoint.longitude
        val position = LatLng.from(lat, lon);

        routeLineManager = kakaoMap.routeLineManager ?: run {
            Log.e("MapFragment", "RouteLineManager를 가져오는 데 실패했습니다.")
            return
        }

        routeLayer = routeLineManager.layer ?: run {
            Log.e("MapFragment", "RouteLineLayer를 가져오는 데 실패했습니다.")
            return
        }

        stylesSet = RouteLineStylesSet.from(
            "blueStyles",
            RouteLineStyles.from(RouteLineStyle.from(16f,Color.BLUE))
        )

        routeSegment = RouteLineSegment.from((toGangam)).setStyles(stylesSet.getStyles(0))
        routeOptions = RouteLineOptions.from(routeSegment)
        routeLayer.addRouteLine(routeOptions)
    }

    companion object {
        //SingleTon Pattern(싱글톤 패턴)
        @Volatile
        private var instance: InformationMarker? = null

        fun getInstance() =
            instance ?: synchronized(InformationMarker::class.java) {
                instance ?: InformationMarker().also {
                    instance = it
                }
            }

        val toGangam = listOf(
            LatLng.from(37.401750, 127.109656),
            LatLng.from(37.396374, 127.109653),
            LatLng.from(37.396247, 127.109676),
            LatLng.from(37.396256, 127.112654),
            LatLng.from(37.392096, 127.112742),
            LatLng.from(37.391975, 127.112752),
            LatLng.from(37.391815, 127.112594),
            LatLng.from(37.391738, 127.111387),
            LatLng.from(37.391771, 127.111023)
        )
    }
}
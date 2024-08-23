package com.example.daejangjung2.feature.main.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingFragment
import com.example.daejangjung2.common.view.showSnackbar
import com.example.daejangjung2.databinding.FragmentMapBinding
import com.example.daejangjung2.domain.model.GeoPoint
import com.example.daejangjung2.feature.main.mypage.MyPageFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapType
import com.kakao.vectormap.MapView
import com.kakao.vectormap.MapViewInfo
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class MapFragment : BindingFragment<FragmentMapBinding>(R.layout.fragment_map) {
    private val viewModel: MapViewModel by viewModels()

    private lateinit var kakaoMap: KakaoMap
    private lateinit var mapView: MapView

    @SuppressLint("MissingPermission")
    private val locationResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true) {
                requestLocationUpdateService()
                return@registerForActivityResult
            }
            if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
                requestLocationUpdateService()
                showSnackBar(R.string.map_location_permission_upgrade_require_message) { navigateToPermissionSetting() }
            } else {
                showSnackBar(R.string.map_location_permission_require_message) { navigateToPermissionSetting() }
            }
        }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val locationRequest = LocationRequest.create().apply {
        interval = 2000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                Log.d(
                    DAEJANGJUNG,
                    "제공자: ${location.provider}, ${location.latitude}, ${location.longitude}",
                )
                viewModel.updateCurPosition(GeoPoint(location.latitude, location.longitude))
                break
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        mapView = binding.kakaoMap
        setupViewModel()
    }

    private fun setupViewModel() {
        viewModel.lastUserPoint.observe(viewLifecycleOwner) {
            mapView.start(object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    // 지도 API가 정상적으로 종료될 때 호출됨
                }

                override fun onMapError(error: Exception) {
                    // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                }
            }, object : KakaoMapReadyCallback() {
                override fun onMapReady(map: KakaoMap) {
                    // 인증 후 API가 정상적으로 실행될 때 호출됨
//                    addMarker(map)
                    kakaoMap = map;
                }

                override fun getPosition(): LatLng {
                    // 지도 시작 시 위치 좌표를 설정
                    return LatLng.from(it.latitude, it.longitude)
                }

                override fun getZoomLevel(): Int {
                    // 지도 시작 시 확대/축소 줌 레벨 설정
                    return 3
                }

                override fun getMapViewInfo(): MapViewInfo {
                    // 지도 시작 시 App 및 MapType 설정
                    return MapViewInfo.from(MapType.NORMAL.toString())
                }

                override fun getViewName(): String {
                    // KakaoMap 의 고유한 이름을 설정
                    return "Daejanjung2"
                }

                override fun isVisible(): Boolean {
                    // 지도 시작 시 visible 여부를 설정
                    return true
                }

                override fun getTag(): String {
                    // KakaoMap 의 tag 을 설정
                    return "Daejanjung2"
                }
            })
        }
    }

    private fun addMarker(kakaoMap: KakaoMap) {
        val styles: LabelStyles = LabelStyles.from(
            "myStyleId",
            LabelStyle.from(R.drawable.ic_map_pin_user_24).setZoomLevel(3),
            LabelStyle.from(R.drawable.ic_map_pin_user_24).setZoomLevel(5),
            LabelStyle.from(R.drawable.ic_map_pin_user_24).setZoomLevel(7)
        );
        kakaoMap.labelManager?.addLabelStyles(styles);
    }

    override fun onResume() {
        super.onResume()
        binding.kakaoMap.resume() // MapView의 resume 호출
    }

    override fun onPause() {
        super.onPause()
        binding.kakaoMap.pause() // MapView의 pause 호출
    }

    private fun showSnackBar(@StringRes messageId: Int, action: () -> Unit) {
        binding.root.showSnackbar(messageId) { action() }
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdateService() {
        if (isAllowedLocationPermission().not()) return
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper(),
        )
    }

    private fun navigateToPermissionSetting() {
        Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", requireActivity().packageName, null)
            startActivity(this)
        }
    }

    // 위치 권한 관련 로직들
    private fun isAllowedLocationPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return false
        }
        return true
    }

    companion object {
        private const val REQUEST_CODE: Int = 1
        private const val DAEJANGJUNG: String = "DAJANGJUNG"

        @JvmStatic
        fun newInstance() = MapFragment()
    }
}
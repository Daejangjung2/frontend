package com.example.daejangjung2.feature.main.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
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
import com.example.daejangjung2.common.view.Toaster
import com.example.daejangjung2.common.view.showSnackbar
import com.example.daejangjung2.databinding.FragmentMapBinding
import com.example.daejangjung2.domain.model.GeoPoint
import com.example.daejangjung2.domain.model.NewsInfo
import com.example.daejangjung2.feature.main.map.newsInfo.NewsFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.route.RouteLineLayer
import com.kakao.vectormap.route.RouteLineManager
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import java.util.Arrays


class MapFragment : BindingFragment<FragmentMapBinding>(R.layout.fragment_map) {
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
    private val viewModel: MapViewModel by viewModels()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val locationRequest = LocationRequest.create().apply {
        interval = 15000000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private lateinit var routeLineManager: RouteLineManager // RouteLineManager 추가

    private lateinit var layer: RouteLineLayer;

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

    private lateinit var kakaoMap: KakaoMap;

    private lateinit var Maps: MapView;

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        Maps = binding.kakaoMap;

        checkLocationPermission()

        Maps.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                TODO("Not yet implemented")
            }

            override fun onMapError(error: java.lang.Exception?) {
                // 인증 실패 및 지도 사용 중 에러 발생 시 호출됨
                error?.printStackTrace()
            }

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakao: KakaoMap) {
                kakaoMap = kakao
//                setupRouteLineManager(kakaoMap)
                setupViewModel()
            }

            override fun getZoomLevel(): Int {
                return 17
            }
        })
    }

    private fun setupRouteLineManager(kakaoMap: KakaoMap) {
        routeLineManager = kakaoMap.getRouteLineManager() ?: run {
            Log.e("MapFragment", "RouteLineManager를 가져오는 데 실패했습니다.")
            return
        }

        layer = routeLineManager.layer ?: run {
            Log.e("MapFragment", "RouteLineLayer를 가져오는 데 실패했습니다.")
            return
        }

        // 예시 경로 데이터 설정 (여기서 실제 경로 데이터를 넣을 수 있음)
        val start = LatLng.from(37.5666805, 126.9784147) // 출발지 좌표 (예시)
        val end = LatLng.from(37.5700457, 126.9828411) // 도착지 좌표 (예시)

        // 2. RouteLineStylesSet 을 생성
        val styles1 = RouteLineStyles.from(RouteLineStyle.from(16f, Color.BLUE))
        val styles2 = RouteLineStyles.from(
            RouteLineStyle.from(16f, Color.BLUE).setZoomLevel(10),
            RouteLineStyle.from(20f, Color.GREEN, 1f, Color.WHITE).setZoomLevel(15)
        )


        val stylesSet = RouteLineStylesSet.from(styles1, styles2)

        val ByLevel2 = Arrays.asList(
            LatLng.from(37.394725518530834, 127.11015051307636),
            LatLng.from(37.401928707331656, 127.10823557165544)
        )


// 3. RouteLineSegment 생성하기 - 세그먼트에 스타일 설정을 생략하면, RouteLineStylesSet 의 index 0 번째에 해당되는 스타일로 설정된다.
        val segments: List<RouteLineSegment> = Arrays.asList(
            RouteLineSegment.from(ByLevel2, stylesSet.getStyles(1)),
        )


// 4. RouteLineStylesSet 을 추가하고 RouteLineOptions 생성하기
        val options = RouteLineOptions.from(segments)
            .setStylesSet(stylesSet)

        val routeLine = layer.addRouteLine(options)

        kakaoMap.moveCamera(
            CameraUpdateFactory.newCenterPosition(start, 21),
            CameraAnimation.from(500)
        )
    }

    override fun onStart() {
        super.onStart()
        requestLocationUpdateService()
    }

    override fun onStop() {
        super.onStop()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        Maps.resume();
    }

    override fun onPause() {
        super.onPause()
        Maps.pause();
    }

    override fun onDestroyView() {
        super.onDestroyView()
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

    private fun checkLocationPermission() {
        if (checkLocationService().not()) {
            showToast(R.string.map_location_service_turn_on_require_message)
        }

        // 둘 중에 한 개도 부여된 것이 없다면,,
        if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                showSnackBar(R.string.map_location_permission_require_message) { navigateToPermissionSetting() }
            } else {
                locationResultLauncher.launch(REQUIRE_LOCATION_PERMISSIONS)
            }
        } else {
            if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                showSnackBar(R.string.map_location_permission_upgrade_require_message) { navigateToPermissionSetting() }
            }
        }
    }

    private fun navigateToPermissionSetting() {
        Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", requireActivity().packageName, null)
            startActivity(this)
        }
    }

    // GPS가 켜져있는지 확인
    private fun checkLocationService(): Boolean {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun showSnackBar(@StringRes messageId: Int, action: () -> Unit) {
        binding.root.showSnackbar(messageId) { action() }
    }

    private fun showToast(@StringRes messageId: Int) {
        Toaster.showShort(requireContext(), requireContext().getString(messageId))
    }

    private fun setupViewModel() {
        viewModel.lastUserPoint.observe(viewLifecycleOwner) { point ->
            point?.let {
                val lat = it.latitude;
                val lon = it.longitude;
                val camera = CameraUpdateFactory.newCenterPosition(LatLng.from(lat, lon),17)
                kakaoMap.moveCamera(camera, CameraAnimation.from(500, true, true));
                val styles =
                    kakaoMap?.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_navigate)))

                val options = LabelOptions.from(LatLng.from(lat, lon)).setStyles(styles);

                val layer = kakaoMap.labelManager?.getLayer();

                layer?.addLabel(options);
            }
        }

        viewModel.event.observe(viewLifecycleOwner) { event ->
            handleEvent(event)
        }
    }

    private fun handleEvent(event: MapViewModel.Event) {
        when(event){
            is MapViewModel.Event.Failed -> TODO()
            is MapViewModel.Event.Service -> {
                when(event.service){
                    ServiceType.GUIDE -> setupButtonListeners(true, false, false, false)
                    ServiceType.WEATHER -> setupButtonListeners(false, true, false, false)
                    ServiceType.NEWS -> {
                        setupButtonListeners(false, false, true, false)
                        NewsFragment().show(childFragmentManager, MAP_NEWS_INFO_TAG)
                    }
                    ServiceType.CONTENT -> setupButtonListeners(false, false, false, true)
                }
            }
            MapViewModel.Event.Success -> TODO()
        }
    }

    private fun setupButtonListeners(isGuide: Boolean, isWeather: Boolean, isNews: Boolean, isContent: Boolean){
        binding.btnGuide.isSelected = isGuide
        binding.btnWeather.isSelected = isWeather
        binding.btnNews.isSelected = isNews
        binding.btnContent.isSelected = isContent
    }

    companion object {
        private const val REQUEST_CODE: Int = 1
        private const val MAP_NEWS_INFO_TAG = "fragment_map_news_info_tag"
        private const val DAEJANGJUNG: String = "DAJANGJUNG"
        private val REQUIRE_LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )

        @JvmStatic
        fun newInstance() = MapFragment()
    }
}
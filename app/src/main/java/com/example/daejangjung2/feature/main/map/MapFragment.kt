package com.example.daejangjung2.feature.main.map

import NewsAdapter
import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingFragment
import com.example.daejangjung2.common.view.Toaster
import com.example.daejangjung2.common.view.showSnackbar
import com.example.daejangjung2.databinding.FragmentMapBinding
import com.example.daejangjung2.domain.model.GeoPoint
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
import com.kakao.vectormap.MapType
import com.kakao.vectormap.MapView
import com.kakao.vectormap.MapViewInfo
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles

class MapFragment : BindingFragment<FragmentMapBinding>(R.layout.fragment_map) {
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

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

        // 버튼 클릭 리스너 설정
        setupButtonListeners()
        setupBottomSheet()

        checkLocationPermission()
        Maps.start(object: MapLifeCycleCallback(){
            override fun onMapDestroy() {
                TODO("Not yet implemented")
            }

            override fun onMapError(error: java.lang.Exception?) {
                // 인증 실패 및 지도 사용 중 에러 발생 시 호출됨
                error?.printStackTrace()
            }

        }, object : KakaoMapReadyCallback(){
            override fun onMapReady(kakao: KakaoMap) {
                kakaoMap = kakao
                setupViewModel()
            }
            override fun getZoomLevel(): Int {
                return 30
            }
        })
    }

    override fun onStart() {
        super.onStart()
        requestLocationUpdateService()
    }

    override fun onStop() {
        super.onStop()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        Maps.resume();
    }

    override fun onPause() {
        super.onPause()
        Maps.pause();
    }

    // 상단 버튼 리스너
    private fun setupButtonListeners() {
        // "안내" 버튼 클릭 리스너
        binding.btnGuide.setOnClickListener {
            // 안내 버튼 클릭 시 동작 정의
            handleGuideButtonClick()

            it.isSelected = true
            binding.btnNews.isSelected = false
            binding.btnWeather.isSelected = false
            binding.btnContent.isSelected = false
        }

        // "날씨" 버튼 클릭 리스너
        binding.btnWeather.setOnClickListener {
            // 날씨 버튼 클릭 시 동작 정의
            handleWeatherButtonClick()

            it.isSelected = true
            binding.btnGuide.isSelected = false
            binding.btnNews.isSelected = false
            binding.btnContent.isSelected = false
        }

        // "뉴스" 버튼 클릭 리스너
        binding.btnNews.setOnClickListener {
            // 뉴스 버튼 클릭 시 동작 정의
            handleNewsButtonClick()

            // 뉴스 버튼을 선택된 상태로 설정
            it.isSelected = true
            binding.btnGuide.isSelected = false
            binding.btnWeather.isSelected = false
            binding.btnContent.isSelected = false
        }

        // "컨텐츠" 버튼 클릭 리스너
        binding.btnContent.setOnClickListener {
            // 컨텐츠 버튼 클릭 시 동작 정의
            handleContentButtonClick()

            it.isSelected = true
            binding.btnGuide.isSelected = false
            binding.btnWeather.isSelected = false
            binding.btnNews.isSelected = false
        }
    }

    private fun handleGuideButtonClick() {
        // "안내" 버튼 클릭 시 수행할 동작
        // 예: 특정 안내 화면으로 이동
        setVisible_bottomsheet()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun handleWeatherButtonClick() {
        // "날씨" 버튼 클릭 시 수행할 동작
        // 예: 날씨 정보를 팝업으로 보여주기
        setVisible_bottomsheet()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun handleNewsButtonClick() {
        // "뉴스" 버튼 클릭 시 수행할 동작
        // 예: 최신 뉴스 보여주기
        // 바텀시트를 보이도록 설정
        val bottomSheet = view?.findViewById<LinearLayout>(R.id.bottom_sheet)
        bottomSheet?.visibility = View.VISIBLE

        binding.recyclerViewNews.adapter = NewsAdapter(getNewsData())
        binding.recyclerViewNews.layoutManager = LinearLayoutManager(requireContext())

        // 바텀시트를 확장 상태로 전환
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun handleContentButtonClick() {
        // "컨텐츠" 버튼 클릭 시 수행할 동작
        // 예: 컨텐츠 화면으로 이동
        setVisible_bottomsheet()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun setupBottomSheet() {
        val bottomSheet = view?.findViewById<LinearLayout>(R.id.bottom_sheet)
        if (bottomSheet != null) {
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

            // 화면 높이의 70%로 바텀시트 높이 설정
            val screenHeight = resources.displayMetrics.heightPixels
            bottomSheet.layoutParams.height = (screenHeight * 0.7).toInt()
            bottomSheet.requestLayout()

            // 바텀시트 초기 상태를 숨김으로 설정
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            Log.e("MapFragment", "Bottom sheet not found!")
        }

        // 바텀시트 상태 변경 리스너 설정 (필요 시)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        // 바텀시트가 확장되었을 때
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        // 바텀시트가 축소되었을 때
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        // 바텀시트가 숨겨졌을 때
                    }
                    else -> {}
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // 바텀시트가 드래그될 때 동작
            }
        })
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
                val camera = CameraUpdateFactory.newCenterPosition(LatLng.from(lat, lon))
                kakaoMap.moveCamera(camera, CameraAnimation.from(500,true,true));
                val styles = kakaoMap?.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_navigate)))

                val options = LabelOptions.from(LatLng.from(lat, lon)).setStyles(styles);

                val layer = kakaoMap.labelManager?.getLayer();

                layer?.addLabel(options);
            }
        }

        viewModel.event.observe(viewLifecycleOwner) { event ->
            handleEvent(event)
        }
    }


    private fun handleEvent(event: MapViewModel.Event){

    }

    private fun getNewsData(): List<NewsItem> {
        return listOf(
            NewsItem("송실로 도로 통제", "내용", R.drawable.sample_image),
            NewsItem("뉴스 내용", "내용", R.drawable.sample_image),
            NewsItem("동작산 산사태 발생", "내용", R.drawable.sample_image),
            NewsItem("위급사항", "내용", R.drawable.sample_image)
            // 더 많은 뉴스 항목 추가 가능
        )
    }

    data class NewsItem(val title: String, val content: String, val imageResId: Int)

    private fun setVisible_bottomsheet() {
        val bottomSheet = view?.findViewById<LinearLayout>(R.id.bottom_sheet)
        bottomSheet?.visibility = View.GONE
    }


    companion object {
        private const val REQUEST_CODE: Int = 1
        private const val DAEJANGJUNG: String = "DAJANGJUNG"
        private val REQUIRE_LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )

        @JvmStatic
        fun newInstance() = MapFragment()
    }
}
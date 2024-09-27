package com.example.daejangjung2.feature.main.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingFragment
import com.example.daejangjung2.common.util.LocationUtils
import com.example.daejangjung2.common.view.Toaster
import com.example.daejangjung2.data.model.response.WeatherResponse
import com.example.daejangjung2.databinding.FragmentMapBinding
import com.example.daejangjung2.domain.model.GeoPoint
import com.example.daejangjung2.feature.main.map.guide.InformationMarker
import com.example.daejangjung2.feature.main.map.newsInfo.NewsFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.TrackingManager
import com.kakao.vectormap.label.TransformMethod
import com.kakao.vectormap.shape.DotPoints
import com.kakao.vectormap.shape.Polygon
import com.kakao.vectormap.shape.PolygonOptions
import com.kakao.vectormap.shape.PolygonStyles
import com.kakao.vectormap.shape.PolygonStylesSet


class MapFragment : BindingFragment<FragmentMapBinding>(R.layout.fragment_map),
    SensorEventListener {

    private val viewModel: MapViewModel by viewModels { MapViewModel.Factory }
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var kakaoMap: KakaoMap
    private lateinit var Maps: MapView
    private lateinit var trackingManager: TrackingManager
    private var locationLabel: Label? = null
    private var headingLabel: Label? = null
    private var circleWavePolygon: Polygon? = null
    private var lastLocation: Location? = null
    private lateinit var sensorManager: SensorManager
    private var rotationSensor: Sensor? = null

    private val informationMarker: InformationMarker by lazy {
        InformationMarker.getInstance()
    }

    private var sensorAccuracy: Int = SensorManager.SENSOR_STATUS_UNRELIABLE

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult ?: return  // null 체크
            for (location in locationResult.locations) {
                Log.d(
                    "DAEJANG",
                    "제공자: ${location.provider}, ${location.latitude}, ${location.longitude}"
                )
                lastLocation = location
                locationLabel?.moveTo(LatLng.from(location.latitude, location.longitude))
                viewModel.updateCurPosition(GeoPoint(location.latitude, location.longitude))
                break
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        Maps = binding.kakaoMap;

        Maps.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                Maps.finish()
            }

            override fun onMapError(error: java.lang.Exception?) {
                error?.printStackTrace()
            }

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakao: KakaoMap) {
                kakaoMap = kakao

                setupViewModel()
            }
        })

        setObserve();
    }

    override fun onResume() {
        super.onResume()
        Maps.resume()

        checkLocationPermission()

        rotationSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }

        if (LocationUtils.isLocationPermissionGranted(requireContext())) {
            val locationRequest =
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    LocationRequest.Builder(1000).build()
                } else {
                    LocationRequest.create().apply {
                        interval = 1000
                        fastestInterval = 500
                        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    }
                }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    override fun onPause() {
        super.onPause()
        Maps.pause()
        stopLocationUpdate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Maps.finish()
        stopLocationUpdate()
    }

    private fun stopLocationUpdate() {
        sensorManager.unregisterListener(this)
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun checkLocationPermission() {
        val requestList = permissions.filter {
            ActivityCompat.checkSelfPermission(
                requireContext(),
                it
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (requestList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                requestList.toTypedArray(),
                PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun setupViewModel() {
        val position = lastLocation?.let {
            LatLng.from(it.latitude, it.longitude)
        } ?: kakaoMap.cameraPosition?.position

        val camera = CameraUpdateFactory.newCenterPosition(position, 17)
        kakaoMap.moveCamera(camera, CameraAnimation.from(500, true, true));

        val labelLayer = kakaoMap.labelManager?.layer
        locationLabel = labelLayer?.addLabel(
            LabelOptions.from(position).setRank(10)
                .setStyles(
                    LabelStyles.from(
                        LabelStyle.from(R.drawable.current_location)
                            .setAnchorPoint(0.5f, 0.5f)
                    )
                )
                .setTransform(TransformMethod.AbsoluteRotation_Decal)
        )

        headingLabel = labelLayer?.addLabel(
            LabelOptions.from(position).setRank(9)
                .setStyles(
                    LabelStyles.from(
                        LabelStyle.from(R.drawable.red_direction_area)
                            .setAnchorPoint(0.5f, 1.0f)
                    )
                )
                .setTransform(TransformMethod.AbsoluteRotation_Decal)
        )

        locationLabel?.addSharePosition(headingLabel)

        circleWavePolygon = kakaoMap.shapeManager?.layer?.addPolygon(
            PolygonOptions.from("circlePolygon")
                .setVisible(false)
                .setDotPoints(DotPoints.fromCircle(position, 1.0f))
                .setStylesSet(PolygonStylesSet.from(PolygonStyles.from(Color.parseColor("#f55d44"))))
        )

        locationLabel?.addShareTransform(circleWavePolygon)
    }

    private fun setObserve() {
        viewModel.event.observe(viewLifecycleOwner) { event ->
            handleEvent(event)
        }
        viewModel.weather.observe(viewLifecycleOwner) { setWeather(it) }
    }

    private fun setWeather(weather: WeatherResponse) {
        Toaster.showShort(requireContext(), weather.humidity)
    }

    private fun handleEvent(event: MapViewModel.Event) {
        when (event) {
            is MapViewModel.Event.Failed -> TODO()
            is MapViewModel.Event.Service -> {
                when (event.service) {
                    ServiceType.GUIDE -> {
                        setupButtonListeners(true, false, false, false)
                        informationMarker.setupRouterLine(kakaoMap, viewModel.lastUserPoint.value!!)
                    }

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

    override fun onSensorChanged(event: SensorEvent?) {
        // 정확도가 높음(SENSOR_STATUS_ACCURACY_HIGH)일 때만 처리
        if (sensorAccuracy == SensorManager.SENSOR_STATUS_ACCURACY_HIGH) {
            if (event?.sensor?.type == Sensor.TYPE_ORIENTATION) {
                val rotationMatrix = FloatArray(9)
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values)

                val orientationAngles = FloatArray(3)
                SensorManager.getOrientation(rotationMatrix, orientationAngles)

                val azimuth = Math.toDegrees(orientationAngles[0].toDouble()).toFloat()

                headingLabel?.rotateTo(azimuth)
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        when (accuracy) {
            SensorManager.SENSOR_STATUS_UNRELIABLE, SensorManager.SENSOR_STATUS_ACCURACY_LOW, SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> {
                // 정확도가 낮거나 신뢰할 수 없으면 변화를 무시
                sensorAccuracy = SensorManager.SENSOR_STATUS_UNRELIABLE
            }

            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> {
                // 정확도가 보통이거나 높으면 허용
                sensorAccuracy = accuracy
            }
        }
    }


    private fun setupButtonListeners(
        isGuide: Boolean,
        isWeather: Boolean,
        isNews: Boolean,
        isContent: Boolean
    ) {
        binding.btnGuide.isSelected = isGuide
        binding.btnWeather.isSelected = isWeather
        binding.btnNews.isSelected = isNews
        binding.btnContent.isSelected = isContent
    }

    companion object {
        private const val PERMISSIONS_REQUEST_CODE = 100
        private const val MAP_NEWS_INFO_TAG = "fragment_map_news_info_tag"
        private val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        @JvmStatic
        fun newInstance() = MapFragment()
    }
}
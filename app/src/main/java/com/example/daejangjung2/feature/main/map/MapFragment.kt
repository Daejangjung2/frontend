package com.example.daejangjung2.feature.main.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import com.google.android.gms.location.LocationRequest
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.example.daejangjung2.R
import com.example.daejangjung2.common.base.BindingFragment
import com.example.daejangjung2.common.util.LocationUtils
import com.example.daejangjung2.common.view.Toaster
import com.example.daejangjung2.common.view.showSnackbar
import com.example.daejangjung2.databinding.FragmentMapBinding
import com.example.daejangjung2.domain.model.GeoPoint
import com.example.daejangjung2.feature.main.map.newsInfo.NewsFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
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


class MapFragment : BindingFragment<FragmentMapBinding>(R.layout.fragment_map) {

    private val viewModel: MapViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var kakaoMap: KakaoMap
    private lateinit var Maps: MapView
    private lateinit var trackingManager: TrackingManager
    private var locationLabel: Label? = null
    private var headingLabel: Label? = null
    private var circleWavePolygon: Polygon? = null

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                Log.d(DAEJANGJUNG, "제공자: ${location.provider}, ${location.latitude}, ${location.longitude}")
                viewModel.updateCurPosition(GeoPoint(location.latitude, location.longitude))
                break
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        Maps = binding.kakaoMap;

        Maps.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
            }

            override fun onMapError(error: java.lang.Exception?) {
                error?.printStackTrace()
            }

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(kakao: KakaoMap) {
                kakaoMap = kakao

                kakaoMap.compass?.show()

                trackingManager = kakaoMap.trackingManager!!

                setupViewModel()
            }

            override fun getZoomLevel(): Int {
                return 17
            }
        })
    }

    override fun onStart() {
        checkLocationPermission()

        if (LocationUtils.isLocationPermissionGranted(requireContext())) {
            val locationRequest = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                LocationRequest.Builder(1000).build()
            } else {
                LocationRequest.create().apply {
                    interval = 1000
                    fastestInterval = 500
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        }
        super.onStart()
    }

    override fun onStop() {
        Maps.pause()
        stopLocationUpdate()
        super.onStop()
    }

    private fun stopLocationUpdate(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun checkLocationPermission() {
        val requestList = permissions.filter {
            ActivityCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }

        if (requestList.isNotEmpty()) {
            ActivityCompat.requestPermissions(requireActivity(), requestList.toTypedArray(), PERMISSIONS_REQUEST_CODE)
        }
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
                val position = LatLng.from(lat, lon)
                val camera = CameraUpdateFactory.newCenterPosition(LatLng.from(lat, lon),17)
                kakaoMap.moveCamera(camera, CameraAnimation.from(500, true, true));
                val labelLayer = kakaoMap.labelManager?.layer
                locationLabel = labelLayer?.addLabel(
                    LabelOptions.from(position).setRank(10)
                        .setStyles(LabelStyles.from(LabelStyle.from(R.drawable.current_location)
                            .setAnchorPoint(0.5f, 0.5f)))
                        .setTransform(TransformMethod.AbsoluteRotation_Decal)
                )

                headingLabel = labelLayer?.addLabel(
                    LabelOptions.from(position).setRank(9)
                        .setStyles(LabelStyles.from(LabelStyle.from(R.drawable.red_direction_area)
                            .setAnchorPoint(0.5f, 1.0f)))
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
        private const val PERMISSIONS_REQUEST_CODE = 100
        private const val MAP_NEWS_INFO_TAG = "fragment_map_news_info_tag"
        private const val DAEJANGJUNG: String = "DAJANGJUNG"
        private val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

        @JvmStatic
        fun newInstance() = MapFragment()
    }
}
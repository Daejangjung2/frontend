package com.example.daejangjung2.app

import android.app.Application
import android.content.Context
import com.example.daejangjung2.data.datasource.local.LocalAuthDataSource
import com.example.daejangjung2.data.datasource.network.NetworkAuthDataSource
import com.example.daejangjung2.data.datasource.network.NetworkCommunityDataSource
import com.example.daejangjung2.data.datasource.network.NetworkMapDataSource
import com.example.daejangjung2.data.datasource.network.NetworkMyPageDataSource
import com.example.daejangjung2.data.datasource.network.NetworkPostCallAllDataSource
import com.example.daejangjung2.data.datasource.network.NetworkPostCallLocationDataSource
import com.example.daejangjung2.data.repository.DefaultAuthRepository
import com.example.daejangjung2.data.repository.DefaultCommunityRepository
import com.example.daejangjung2.data.repository.DefaultMapRepository
import com.example.daejangjung2.data.repository.DefaultMyPageRepository
import com.example.daejangjung2.data.repository.DefaultPostCallAllRepository
import com.example.daejangjung2.data.retrofit.AuthRetrofit
import com.example.daejangjung2.data.retrofit.AuthService
import com.example.daejangjung2.data.retrofit.CommunityService
import com.example.daejangjung2.data.retrofit.DefaultRetrofit
import com.example.daejangjung2.data.retrofit.MapService
import com.example.daejangjung2.data.retrofit.MyPageService
import com.example.daejangjung2.data.retrofit.PostCallAllService
import com.example.daejangjung2.data.retrofit.PostCallLocationService
import com.example.daejangjung2.domain.repository.AuthRepository
import com.example.daejangjung2.domain.repository.CommunityRepository
import com.example.daejangjung2.domain.repository.MapRepository
import com.example.daejangjung2.domain.repository.MyPageRepository
import com.example.daejangjung2.domain.repository.PostCallAllRepository
import retrofit2.create

class DIContainer(
    application: Application,
    deviceId: String,
) {
    val sharedPref = application.getSharedPreferences(
        LocalAuthDataSource.AUTH_TOKEN_STORE_NAME, Context.MODE_PRIVATE)

    private val localAuthDataSource: LocalAuthDataSource =
        LocalAuthDataSource(sharedPref)


    // 로그인
    private val authClient = AuthRetrofit.createInstance()
    private val authService = authClient.create(AuthService::class.java)
    private val networkAuthDataSource = NetworkAuthDataSource(authService)
    val authRepository: AuthRepository =
        DefaultAuthRepository(localAuthDataSource, networkAuthDataSource)
    val isLogin = authRepository.isLogin

    // 공통 accessToken 적용 retrofit
    private val default = DefaultRetrofit.createInstance(authRepository)

    // 맵
    private val mapService = default.create(MapService::class.java)
    private val networkMapDataSource = NetworkMapDataSource(mapService)
    val mapRepository: MapRepository = DefaultMapRepository(networkMapDataSource)

    // 커뮤니티
    private val communityService = default.create(CommunityService::class.java)
    private val networkCommunityDataSource = NetworkCommunityDataSource(communityService)
    val communityRepository: CommunityRepository = DefaultCommunityRepository(networkCommunityDataSource)

    // 모든 포스트 부르기
    private val postcallallService = default.create(PostCallAllService::class.java)
    private val networkpostcallallDataSource = NetworkPostCallAllDataSource(postcallallService)
    // 지역별 포스트 부르기
    private val postcalllocationService = default.create(PostCallLocationService::class.java)
    private val networkpostcalllocationDataSource = NetworkPostCallLocationDataSource(postcalllocationService)
    val postcallallRepository: PostCallAllRepository = DefaultPostCallAllRepository(networkpostcallallDataSource, networkpostcalllocationDataSource)


    val postcalllocationRepository: PostCallAllRepository = DefaultPostCallAllRepository(networkpostcallallDataSource, networkpostcalllocationDataSource)

    // 마이페이지
    private val myPageService = default.create(MyPageService::class.java)
    private val networkMyPageDataSource = NetworkMyPageDataSource(myPageService)
    val myPageRepository: MyPageRepository = DefaultMyPageRepository(networkMyPageDataSource)

}
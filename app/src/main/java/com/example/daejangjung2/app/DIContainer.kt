package com.example.daejangjung2.app

import android.app.Application
import android.content.Context
import com.example.daejangjung2.data.datasource.local.LocalAuthDataSource
import com.example.daejangjung2.data.datasource.network.NetworkAuthDataSource
import com.example.daejangjung2.data.datasource.network.NetworkMapDataSource
import com.example.daejangjung2.data.repository.DefaultAuthRepository
import com.example.daejangjung2.data.repository.DefaultMapRepository
import com.example.daejangjung2.data.retrofit.AuthRetrofit
import com.example.daejangjung2.data.retrofit.AuthService
import com.example.daejangjung2.data.retrofit.DefaultRetrofit
import com.example.daejangjung2.data.retrofit.MapService
import com.example.daejangjung2.domain.repository.AuthRepository
import com.example.daejangjung2.domain.repository.MapRepository

class DIContainer(
    application: Application,
    deviceId: String,
) {
    val sharedPref = application.getSharedPreferences(
        LocalAuthDataSource.AUTH_TOKEN_STORE_NAME, Context.MODE_PRIVATE)

    private val localAuthDataSource: LocalAuthDataSource =
        LocalAuthDataSource(sharedPref)

    private val authClient = AuthRetrofit.createInstance()
    private val authService = authClient.create(AuthService::class.java)
    private val networkAuthDataSource = NetworkAuthDataSource(authService)
    val authRepository: AuthRepository =
        DefaultAuthRepository(localAuthDataSource, networkAuthDataSource)

    // 공통 accessToken 적용 retrofit
    private val default = DefaultRetrofit.createInstance(authRepository)

    private val mapService = default.create(MapService::class.java)
    private val networkMapDataSource = NetworkMapDataSource(mapService)
    val mapRepository: MapRepository = DefaultMapRepository(networkMapDataSource)

    val isLogin = authRepository.isLogin

}
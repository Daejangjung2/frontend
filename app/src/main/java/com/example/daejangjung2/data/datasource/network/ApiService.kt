package com.example.daejangjung2.data.datasource.network

import com.example.daejangjung2.data.model.request.KakaoLoginRequest
import com.example.daejangjung2.data.model.response.KakaoLoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

// ApiService.kt
interface ApiService {
    @GET("/api/kakao/auth/login/oauth2")
    fun loginWithKakao(@Query("accessToken") accessToken: String): Call<KakaoLoginResponse>
}

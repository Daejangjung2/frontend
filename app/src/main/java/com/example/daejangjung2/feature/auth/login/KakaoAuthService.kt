package com.example.daejangjung2.feature.auth.login

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoAuthService {
    @GET("oauth/authorize")
    fun getAuthorizationCode(
        @Query("client_id") clientId: String,
        @Query("redirect_uri") redirectUri: String,
        @Query("response_type") responseType: String = "code"
    ): Call<Void>
}

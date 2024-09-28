package com.example.daejangjung2.data.retrofit

import com.example.daejangjung2.data.model.request.LoginRequest
import com.example.daejangjung2.data.model.request.RefreshTokenRequest
import com.example.daejangjung2.data.model.response.Token
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.feature.main.community.AuthResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthService {
    @POST("/api/login/auth/signin")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): ApiResponse<Token>

    @POST("/api/login/auth/refresh")
    suspend fun refresh(
        @Body token: RefreshTokenRequest
    ): Token
}

interface AuthServiceToken {
    @GET("/api/Login/auth/signup/{loginEmail}")
    suspend fun getToken(@Path("loginEmail") loginEmail: String
    ): Response<AuthResponse>
}




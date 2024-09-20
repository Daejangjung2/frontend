package com.example.daejangjung2.data.retrofit

import com.example.daejangjung2.data.model.request.LoginRequest
import com.example.daejangjung2.data.model.request.RefreshTokenRequest
import com.example.daejangjung2.data.model.response.Token
import com.example.daejangjung2.domain.model.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

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
package com.example.daejangjung2.data.datasource.network

import com.example.daejangjung2.data.model.request.LoginRequest
import com.example.daejangjung2.data.model.request.RefreshTokenRequest
import com.example.daejangjung2.data.model.response.LogoutResponse
import com.example.daejangjung2.data.model.response.Token
import com.example.daejangjung2.data.retrofit.AuthService
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse

class NetworkAuthDataSource(
    private val service: AuthService
) {
    suspend fun login(loginRequest: LoginRequest): ApiResponse<Token>{
        return service.login(loginRequest);
    }

    suspend fun refresh(assessToken: RefreshTokenRequest): Token{
        return service.refresh(assessToken)
    }

    suspend fun kakaoLogin(accessToken: String): ApiResponse<Token>{
        return service.kakaoLogin(accessToken)
    }

    suspend fun logout(accessToken: String): ApiResponse<DefaultResponse<Unit>>{
        return service.logout(accessToken)
    }
}
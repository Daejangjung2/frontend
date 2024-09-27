package com.example.daejangjung2.data.datasource.network

import com.example.daejangjung2.data.model.request.LoginRequest
import com.example.daejangjung2.data.model.request.RefreshTokenRequest
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

    suspend fun refresh(refreshToken: RefreshTokenRequest): Token{
        return service.refresh(refreshToken)
    }

    suspend fun logout(): ApiResponse<DefaultResponse<Unit>>{
        return service.logout()
    }
}
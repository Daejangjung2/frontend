package com.example.daejangjung2.data.datasource.network

import com.example.daejangjung2.data.model.request.LoginRequest
import com.example.daejangjung2.data.model.response.Token
import com.example.daejangjung2.data.retrofit.AuthService
import com.example.daejangjung2.domain.model.ApiResponse

class NetworkAuthDataSource(
    private val service: AuthService
) {
    suspend fun login(loginRequest: LoginRequest): ApiResponse<Token>{
        return service.login(loginRequest);
    }
}
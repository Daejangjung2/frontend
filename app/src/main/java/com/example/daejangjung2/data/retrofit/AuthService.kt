package com.example.daejangjung2.data.retrofit

import com.example.daejangjung2.data.model.request.LoginRequest
import com.example.daejangjung2.data.model.response.Token
import com.example.daejangjung2.domain.model.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/user/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): ApiResponse<Token>
}
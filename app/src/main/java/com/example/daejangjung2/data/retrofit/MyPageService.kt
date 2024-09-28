package com.example.daejangjung2.data.retrofit

import com.example.daejangjung2.data.model.response.ProfileResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse
import retrofit2.http.GET

interface MyPageService {
    @GET("/api/login/profile")
    suspend fun profile(): ApiResponse<DefaultResponse<ProfileResponse>>
}
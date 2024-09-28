package com.example.daejangjung2.data.datasource.network

import com.example.daejangjung2.data.model.response.ProfileResponse
import com.example.daejangjung2.data.retrofit.MyPageService
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse

class NetworkMyPageDataSource(
    private val service: MyPageService
) {
    suspend fun profile(): ApiResponse<DefaultResponse<ProfileResponse>>{
        return service.profile()
    }
}
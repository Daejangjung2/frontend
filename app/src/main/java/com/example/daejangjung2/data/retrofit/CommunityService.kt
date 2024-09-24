package com.example.daejangjung2.data.retrofit

import com.example.daejangjung2.data.model.request.CreateRequest
import com.example.daejangjung2.data.model.response.CreateResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse
import retrofit2.http.Body
import retrofit2.http.POST


interface CommunityService {
    @POST("/api/community/create")
    suspend fun create(
        @Body request: CreateRequest
    ): ApiResponse<DefaultResponse<CreateResponse>>
}
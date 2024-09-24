package com.example.daejangjung2.data.datasource.network

import com.example.daejangjung2.data.model.request.CreateRequest
import com.example.daejangjung2.data.model.response.CreateResponse
import com.example.daejangjung2.data.retrofit.CommunityService
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse

class NetworkCommunityDataSource(
    private val service: CommunityService
) {
    suspend fun create(createRequest: CreateRequest): ApiResponse<DefaultResponse<CreateResponse>>{
        return service.create(createRequest)
    }
}
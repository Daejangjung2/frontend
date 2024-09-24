package com.example.daejangjung2.data.datasource.network

import com.example.daejangjung2.data.model.response.PostCallAllResponse
import com.example.daejangjung2.data.retrofit.PostCallAllService
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse

class NetworkPostCallAllDataSource(
    private val service: PostCallAllService
) {
    suspend fun postcallall(): ApiResponse<DefaultResponse<List<PostCallAllResponse>>> {
        return service.PostCallAll()
    }
}
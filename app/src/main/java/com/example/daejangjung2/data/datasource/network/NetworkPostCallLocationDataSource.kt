package com.example.daejangjung2.data.datasource.network

import com.example.daejangjung2.data.model.response.PostCallAllResponse
import com.example.daejangjung2.data.model.response.PostCallLocationResponse
import com.example.daejangjung2.data.retrofit.PostCallAllService
import com.example.daejangjung2.data.retrofit.PostCallLocationService
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse

class NetworkPostCallLocationDataSource(
    private val service: PostCallLocationService
) {
    suspend fun postcalllocation(location: String, page: Int, size: Int): ApiResponse<DefaultResponse<List<PostCallLocationResponse>>> {
        return service.PostCallLocation(location, page, size)
    }
}
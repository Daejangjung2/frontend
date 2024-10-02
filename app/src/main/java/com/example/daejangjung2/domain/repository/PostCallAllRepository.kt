package com.example.daejangjung2.domain.repository

import com.example.daejangjung2.data.model.response.PostCallAllResponse
import com.example.daejangjung2.data.model.response.PostCallLocationResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse

interface PostCallAllRepository {
    suspend fun postcallall(): ApiResponse<DefaultResponse<List<PostCallAllResponse>>>
}

interface PostCallLocationRepository {
    suspend fun postcalllocation(location: String, page: Int, size: Int): ApiResponse<DefaultResponse<PostCallLocationResponse>>
}
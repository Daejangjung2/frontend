package com.example.daejangjung2.domain.repository

import com.example.daejangjung2.data.model.request.CreateRequest
import com.example.daejangjung2.data.model.response.CreateResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse

interface CommunityRepository {
    suspend fun create(createRequest: CreateRequest): ApiResponse<DefaultResponse<CreateResponse>>
}
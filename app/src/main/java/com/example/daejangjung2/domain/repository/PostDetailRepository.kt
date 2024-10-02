package com.example.daejangjung2.domain.repository

import com.example.daejangjung2.data.model.response.DetailPostResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse

interface PostDetailRepository {
    suspend fun postdetail(pageId: Int): ApiResponse<DefaultResponse<DetailPostResponse>>
}
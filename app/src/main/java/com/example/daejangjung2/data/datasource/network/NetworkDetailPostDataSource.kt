package com.example.daejangjung2.data.datasource.network

import com.example.daejangjung2.data.model.response.DetailPostResponse
import com.example.daejangjung2.data.retrofit.PostDetailService
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse

class NetworkDetailPostDataSource(
    private val service: PostDetailService
) {
    suspend fun postdetail(pageId: Int): ApiResponse<DefaultResponse<DetailPostResponse>> {
        return service.PostDetail(pageId)
    }
}
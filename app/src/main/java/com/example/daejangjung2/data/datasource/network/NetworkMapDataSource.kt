package com.example.daejangjung2.data.datasource.network

import com.example.daejangjung2.data.model.response.NewsResponse
import com.example.daejangjung2.data.retrofit.MapService
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse

class NetworkMapDataSource(
    private val service: MapService
) {
    suspend fun news(keywordText: String, count: Int): ApiResponse<DefaultResponse<NewsResponse>>{
        return service.news(keywordText, count)
    }
}
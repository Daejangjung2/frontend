package com.example.daejangjung2.domain.repository

import com.example.daejangjung2.data.model.response.NewsResponse
import com.example.daejangjung2.data.model.response.WeatherResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse

interface MapRepository {
    suspend fun news(keywordText: String, count: Int): ApiResponse<DefaultResponse<List<NewsResponse>>>
    suspend fun weather(step1: String, step2: String, step3: String? = ""): ApiResponse<DefaultResponse<WeatherResponse>>
}
package com.example.daejangjung2.data.retrofit

import com.example.daejangjung2.data.model.response.NewsResponse
import com.example.daejangjung2.data.model.response.WeatherResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface MapService {
    @GET("/api/news")
    suspend fun news(
        @Query("keywordText") keywordText: String,
        @Query("count") count: Int,
    ): ApiResponse<DefaultResponse<List<NewsResponse>>>

    @GET("/api/weather/short")
    suspend fun weather(
        @Query("Step1") step1: String,
        @Query("Step2") step2: String,
        @Query("Step3") step3: String? = ""
    ): ApiResponse<DefaultResponse<WeatherResponse>>
}
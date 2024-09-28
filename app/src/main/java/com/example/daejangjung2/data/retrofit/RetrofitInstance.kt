package com.example.daejangjung2.data.retrofit

import com.example.daejangjung2.data.datasource.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // Retrofit 인스턴스 생성
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://43.201.170.204:8080/") // 여기에 백엔드 API의 기본 URL 입력
            .addConverterFactory(GsonConverterFactory.create()) // Gson으로 JSON을 변환
            .build()
    }

    // ApiService 생성
    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
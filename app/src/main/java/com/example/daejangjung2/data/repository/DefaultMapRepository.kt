package com.example.daejangjung2.data.repository

import android.util.Log
import com.example.daejangjung2.data.datasource.network.NetworkMapDataSource
import com.example.daejangjung2.data.model.response.NewsResponse
import com.example.daejangjung2.data.model.response.WeatherResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse
import com.example.daejangjung2.domain.repository.MapRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class DefaultMapRepository(
    private val networkMapDateSource: NetworkMapDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): MapRepository {
    override suspend fun news(keywordText: String, count: Int): ApiResponse<DefaultResponse<List<NewsResponse>>> {
        return withContext(dispatcher){
            val response = networkMapDateSource.news(keywordText, count)

            Log.d(HTTP_LOG_TAG, response.toString())
            if (response is ApiResponse.Success) {
                Log.d(HTTP_LOG_TAG,"Success")
            }

            response
        }
    }

    override suspend fun weather(
        step1: String,
        step2: String,
        step3: String?
    ): ApiResponse<DefaultResponse<WeatherResponse>> {
        return withContext(dispatcher){
            val response = networkMapDateSource.weather(step1,step2,step3)

            Log.d(HTTP_LOG_TAG, response.toString())
            if (response is ApiResponse.Success) {
                Log.d(HTTP_LOG_TAG,"Success")
            }

            response
        }
    }


    companion object{
        private val HTTP_LOG_TAG = "HTTP_LOG"
    }
}
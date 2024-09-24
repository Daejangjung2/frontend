package com.example.daejangjung2.data.repository

import android.util.Log
import com.example.daejangjung2.data.datasource.network.NetworkPostCallAllDataSource
import com.example.daejangjung2.data.datasource.network.NetworkPostCallLocationDataSource
import com.example.daejangjung2.data.model.response.PostCallAllResponse
import com.example.daejangjung2.data.model.response.PostCallLocationResponse
import com.example.daejangjung2.data.repository.DefaultPostCallAllRepository.Companion.HTTP_LOG_TAG
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse
import com.example.daejangjung2.domain.repository.PostCallAllRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultPostCallAllRepository(
    private val networkPostCallAllDataSource: NetworkPostCallAllDataSource,
    private val networkPostCallLocationDataSource: NetworkPostCallLocationDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): PostCallAllRepository {
    override suspend fun postcallall(): ApiResponse<DefaultResponse<List<PostCallAllResponse>>> {
        return withContext(dispatcher) {
            val response = networkPostCallAllDataSource.postcallall()

            Log.d(HTTP_LOG_TAG, response.toString())
            if (response is ApiResponse.Success) {
                Log.d(HTTP_LOG_TAG,"Success")
            }

            response
        }
    }

    override suspend fun postcalllocation(location: String, page: Int, size: Int): ApiResponse<DefaultResponse<List<PostCallLocationResponse>>> {
        return withContext(dispatcher) {
            val response = networkPostCallLocationDataSource.postcalllocation(location, page, size)

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
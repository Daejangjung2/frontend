package com.example.daejangjung2.data.repository

import android.util.Log
import com.example.daejangjung2.data.datasource.network.NetworkMyPageDataSource
import com.example.daejangjung2.data.model.response.ProfileResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse
import com.example.daejangjung2.domain.repository.MyPageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultMyPageRepository(
    private val networkMyPageDataSource: NetworkMyPageDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): MyPageRepository {
    override suspend fun profile(): ApiResponse<DefaultResponse<ProfileResponse>> {
        return withContext(dispatcher){
            val response = networkMyPageDataSource.profile()

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
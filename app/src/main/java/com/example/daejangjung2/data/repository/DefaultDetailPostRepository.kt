package com.example.daejangjung2.data.repository

import android.util.Log
import com.example.daejangjung2.data.datasource.network.NetworkDetailPostDataSource
import com.example.daejangjung2.data.model.response.DetailPostResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse
import com.example.daejangjung2.domain.repository.PostDetailRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultDetailPostRepository(
    private val networkDetailDataSource: NetworkDetailPostDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): PostDetailRepository {
    override suspend fun postdetail(pageId: Int): ApiResponse<DefaultResponse<DetailPostResponse>> {
        return withContext(dispatcher) {
            val response = networkDetailDataSource.postdetail(pageId)

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
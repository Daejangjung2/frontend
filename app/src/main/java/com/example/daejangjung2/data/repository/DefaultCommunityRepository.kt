package com.example.daejangjung2.data.repository

import android.util.Log
import com.example.daejangjung2.data.datasource.network.NetworkCommunityDataSource
import com.example.daejangjung2.data.model.request.CreateRequest
import com.example.daejangjung2.data.model.response.CreateResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse
import com.example.daejangjung2.domain.repository.CommunityRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultCommunityRepository(
    private val networkCommunityDataSource: NetworkCommunityDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): CommunityRepository {
    override suspend fun create(createRequest: CreateRequest): ApiResponse<DefaultResponse<CreateResponse>> {
        return withContext(dispatcher){
            val response = networkCommunityDataSource.create(
                CreateRequest(
                    image_url = createRequest.image_url,
                    title = createRequest.title,
                    contents = createRequest.contents,
                    location = createRequest.location
                )
            )

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
package com.example.daejangjung2.data.retrofit

import com.example.daejangjung2.data.model.request.CreateRequest
import com.example.daejangjung2.data.model.response.CreateResponse
import com.example.daejangjung2.data.model.response.PostCallAllResponse
import com.example.daejangjung2.data.model.response.PostCallLocationResponse
import com.example.daejangjung2.data.model.response.PostContent
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface PostCallAllService {
    @GET("/api/community/posts")
    suspend fun PostCallAll(
    ): ApiResponse<DefaultResponse<List<PostCallAllResponse>>>
}

interface PostCallLocationService {
    @GET("/api/community/location")
    suspend fun PostCallLocation(
        @Query("location") location: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): ApiResponse<DefaultResponse<PostCallLocationResponse>>
}
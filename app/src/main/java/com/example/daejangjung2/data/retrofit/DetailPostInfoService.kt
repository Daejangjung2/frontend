package com.example.daejangjung2.data.retrofit

import com.example.daejangjung2.data.model.request.CommentDeleteRequest
import com.example.daejangjung2.data.model.request.CommentModifyRequest
import com.example.daejangjung2.data.model.request.CommentRequest
import com.example.daejangjung2.data.model.request.CreateRequest
import com.example.daejangjung2.data.model.request.DeleteRequest
import com.example.daejangjung2.data.model.request.ModifyRequest
import com.example.daejangjung2.data.model.response.CommentDeleteResponse
import com.example.daejangjung2.data.model.response.CommentModifyResponse
import com.example.daejangjung2.data.model.response.CommentResponse
import com.example.daejangjung2.data.model.response.CreateResponse
import com.example.daejangjung2.data.model.response.DeleteResponse
import com.example.daejangjung2.data.model.response.ModifyResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ModifyService {
    @POST("/api/community/modify")
    suspend fun modify(
        @Body request: ModifyRequest
    ): ApiResponse<DefaultResponse<ModifyResponse>>
}

interface DeleteService {
    @POST("/api/community/delete")
    suspend fun delete(
        @Body request: DeleteRequest
    ): ApiResponse<DeleteResponse>
}

interface CommentService {
    @POST("/api/community/comment")
    suspend fun comment(
        @Body request: CommentRequest
    ): ApiResponse<DefaultResponse<CommentResponse>>
}

interface CommentModifyService {
    @POST("/api/community/comment/modify")
    suspend fun commentmodify(
        @Body request: CommentModifyRequest
    ): ApiResponse<DefaultResponse<CommentModifyResponse>>
}

interface CommentDeleteService {
    @POST("/api/community/comment/delete")
    suspend fun commentdelete(
        @Body request: CommentDeleteRequest
    ): ApiResponse<CommentDeleteResponse>
}
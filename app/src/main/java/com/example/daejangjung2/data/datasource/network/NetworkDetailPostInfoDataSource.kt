package com.example.daejangjung2.data.datasource.network

import com.example.daejangjung2.data.model.request.CommentDeleteRequest
import com.example.daejangjung2.data.model.request.CommentModifyRequest
import com.example.daejangjung2.data.model.request.CommentRequest
import com.example.daejangjung2.data.model.request.DeleteRequest
import com.example.daejangjung2.data.model.request.ModifyRequest
import com.example.daejangjung2.data.model.response.CommentDeleteResponse
import com.example.daejangjung2.data.model.response.CommentModifyResponse
import com.example.daejangjung2.data.model.response.CommentResponse
import com.example.daejangjung2.data.model.response.DeleteResponse
import com.example.daejangjung2.data.model.response.ModifyResponse
import com.example.daejangjung2.data.model.response.PostCallAllResponse
import com.example.daejangjung2.data.retrofit.CommentDeleteService
import com.example.daejangjung2.data.retrofit.CommentModifyService
import com.example.daejangjung2.data.retrofit.CommentService
import com.example.daejangjung2.data.retrofit.DeleteService
import com.example.daejangjung2.data.retrofit.ModifyService
import com.example.daejangjung2.data.retrofit.PostCallAllService
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse

class NetworkModifyDataSource(
    private val service: ModifyService
) {
    suspend fun modify(modifyRequest: ModifyRequest): ApiResponse<DefaultResponse<ModifyResponse>> {
        return service.modify(modifyRequest)
    }
}

class NetworkDeleteDataSource(
    private val service: DeleteService
) {
    suspend fun delete(deleteRequest: DeleteRequest): ApiResponse<DeleteResponse> {
        return service.delete(deleteRequest)
    }
}

class NetworkCommentDataSource(
    private val service: CommentService
) {
    suspend fun comment(commentRequest: CommentRequest): ApiResponse<DefaultResponse<CommentResponse>> {
        return service.comment(commentRequest)
    }
}

class NetworkCommentModifyDataSource(
    private val service: CommentModifyService
) {
    suspend fun commentmodify(commentModifyRequest: CommentModifyRequest): ApiResponse<DefaultResponse<CommentModifyResponse>> {
        return service.commentmodify(commentModifyRequest)
    }
}

class NetworkCommentDeleteDataSource(
    private val service: CommentDeleteService
) {
    suspend fun commentdelete(commentDeleteRequest: CommentDeleteRequest): ApiResponse<CommentDeleteResponse> {
        return service.commentdelete(commentDeleteRequest)
    }
}
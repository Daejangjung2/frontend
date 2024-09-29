package com.example.daejangjung2.domain.repository

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
import com.example.daejangjung2.data.model.response.PostCallLocationResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse

interface ModifyRepository {
    suspend fun modify(modifyRequest: ModifyRequest): ApiResponse<DefaultResponse<ModifyResponse>>
}

interface DeleteRepository {
    suspend fun delete(deleteRequest: DeleteRequest): ApiResponse<DeleteResponse>
}

interface CommentRepository {
    suspend fun comment(commentRequest: CommentRequest): ApiResponse<DefaultResponse<CommentResponse>>
}

interface CommentModifyRepository {
    suspend fun commentmodify(commentModifyRequest: CommentModifyRequest): ApiResponse<DefaultResponse<CommentModifyResponse>>
}

interface CommentDeleteRepository {
    suspend fun commentdelete(commentDeleteRequest: CommentDeleteRequest): ApiResponse<CommentDeleteResponse>
}
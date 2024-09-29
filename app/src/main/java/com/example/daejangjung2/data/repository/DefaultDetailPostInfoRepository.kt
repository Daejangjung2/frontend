package com.example.daejangjung2.data.repository

import android.util.Log
import com.example.daejangjung2.data.datasource.network.NetworkCommentDataSource
import com.example.daejangjung2.data.datasource.network.NetworkCommentDeleteDataSource
import com.example.daejangjung2.data.datasource.network.NetworkCommentModifyDataSource
import com.example.daejangjung2.data.datasource.network.NetworkCommunityDataSource
import com.example.daejangjung2.data.datasource.network.NetworkDeleteDataSource
import com.example.daejangjung2.data.datasource.network.NetworkModifyDataSource
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
import com.example.daejangjung2.domain.repository.CommentDeleteRepository
import com.example.daejangjung2.domain.repository.CommentModifyRepository
import com.example.daejangjung2.domain.repository.CommentRepository
import com.example.daejangjung2.domain.repository.CommunityRepository
import com.example.daejangjung2.domain.repository.DeleteRepository
import com.example.daejangjung2.domain.repository.ModifyRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DefaultModifyRepository(
    private val networkModifyDataSource: NetworkModifyDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): ModifyRepository {
    override suspend fun modify(modifyRequest: ModifyRequest): ApiResponse<DefaultResponse<ModifyResponse>> {
        return withContext(dispatcher){
            val response = networkModifyDataSource.modify(
                ModifyRequest(
                    postId = modifyRequest.postId,
                    title = modifyRequest.title,
                    contents = modifyRequest.contents,
                    location = modifyRequest.location,
                    image_url = modifyRequest.image_url
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

class DefaultDeleteRepository(
    private val networkDeleteDataSource: NetworkDeleteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): DeleteRepository {
    override suspend fun delete(deleteRequest: DeleteRequest): ApiResponse<DeleteResponse> {
        return withContext(dispatcher){
            val response = networkDeleteDataSource.delete(
                DeleteRequest(
                    postId = deleteRequest.postId
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

class DefaultCommentRepository(
    private val networkCommentDataSource: NetworkCommentDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): CommentRepository {
    override suspend fun comment(commentRequest: CommentRequest): ApiResponse<DefaultResponse<CommentResponse>> {
        return withContext(dispatcher){
            val response = networkCommentDataSource.comment(
                CommentRequest(
                    postId = commentRequest.postId,
                    comment =commentRequest.comment
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

class DefaultCommentModifyRepository(
    private val networkCommentModifyDataSource: NetworkCommentModifyDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): CommentModifyRepository {
    override suspend fun commentmodify(commentModifyRequest: CommentModifyRequest): ApiResponse<DefaultResponse<CommentModifyResponse>> {
        return withContext(dispatcher){
            val response = networkCommentModifyDataSource.commentmodify(
                CommentModifyRequest(
                    postId = commentModifyRequest.postId,
                    commentId = commentModifyRequest.commentId,
                    comment =commentModifyRequest.comment
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

class DefaultCommentDeleteRepository(
    private val networkCommentDeleteDataSource: NetworkCommentDeleteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): CommentDeleteRepository {
    override suspend fun commentdelete(commentDeleteRequest: CommentDeleteRequest): ApiResponse<CommentDeleteResponse> {
        return withContext(dispatcher){
            val response = networkCommentDeleteDataSource.commentdelete(
                CommentDeleteRequest(
                    commentId = commentDeleteRequest.commentId
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
package com.example.daejangjung2.data.model.request

import com.example.daejangjung2.data.model.response.CommunityComment
import kotlinx.serialization.Serializable

@Serializable
data class ModifyRequest(
    val postId: Int,
    val title: String,
    val contents: String,
    val location: String,
    val image_url: String
)

@Serializable
data class DeleteRequest(
    val postId: Int
)

@Serializable
data class CommentRequest(
    val postId: Int,
    val comment: String
)

@Serializable
data class CommentModifyRequest(
    val postId: Int,
    val commentId: Int,
    val comment: String
)

@Serializable
data class CommentDeleteRequest(
    val commentId: Int
)
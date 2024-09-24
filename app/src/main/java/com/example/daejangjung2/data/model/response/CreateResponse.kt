package com.example.daejangjung2.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CreateResponse(
    val postId: Int,
    val image_url: String,
    val title: String?,
    val contents: String,
    val location: String,
    val communityComment: String?,
    val createdAt: String,
    val updatedAt: String,
    val view: Int?
)

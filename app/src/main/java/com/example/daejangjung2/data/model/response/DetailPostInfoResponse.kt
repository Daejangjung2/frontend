package com.example.daejangjung2.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ModifyResponse(
    val postId: Int,                 // 게시물 ID
    val image_url: String,           // 이미지 URL
    val title: String,               // 게시물 제목
    val contents: String,            // 게시물 내용
    val location: String,            // 게시물 위치
    val communityComment: List<CommunityComment>, // 댓글 리스트 (빈 배열로 표시됨)
    val createdAt: String,           // 생성 날짜 및 시간
    val updatedAt: String,           // 업데이트 날짜 및 시간
    val view: Int                    // 조회수
)

@Serializable
data class DeleteResponse(
    val message: String,    // 예: "Operation completed successfully."
    val code: Int,          // 예: 200
    val data: String?,       // 예: "success"
    val success: Boolean    // 예: true
)

@Serializable
data class CommentResponse(
    val postId: Int,
    val image_url: String,
    val title: String,
    val contents: String,
    val location: String,
    val communityComment: List<CommunityComment>, // 댓글 리스트
    val createdAt: String,
    val updatedAt: String,
    val view: Int
)


@Serializable
data class CommentModifyResponse(
    val postId: Int,
    val image_url: String,
    val title: String,
    val contents: String,
    val location: String,
    val communityComment: List<CommunityComment>,
    val createdAt: String,
    val updatedAt: String,
    val view: Int
)

@Serializable
data class CommentDeleteResponse(
    val message: String,    // 예: "Operation completed successfully."
    val code: Int,          // 예: 200
    val data: String?,       // 예: "success"
    val success: Boolean    // 예: true
)
package com.example.daejangjung2.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class DetailPostResponse(
    val postId: Int,                // 게시물 ID
    val image_url: String?,          // 이미지 URL
    val title: String?,              // 제목
    val contents: String?,           // 내용
    val location: String?,           // 위치
    val communityComment: List<CommunityComment>,  // 댓글 리스트 (비어 있을 수 있음)
    val createdAt: String,          // 생성 날짜 및 시간 (ISO 8601 형식)
    val updatedAt: String,          // 업데이트 날짜 및 시간
    val view: Int?                   // 조회수
)
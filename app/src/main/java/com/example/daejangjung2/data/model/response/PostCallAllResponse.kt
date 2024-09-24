package com.example.daejangjung2.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class CommunityComment(
    val id: Int,        // 댓글 ID
    val content: String // 댓글 내용
)

@Serializable
data class PostCallAllResponse(
    val createdAt: String,  // 날짜와 시간을 String으로 받음 (ISO 8601 형식)
    val updateAt: String,   // 업데이트된 날짜와 시간도 String으로 받음
    val postId: Int,        // 게시글 ID는 Int로
    val communityComments: List<CommunityComment>,  // 댓글은 리스트로 받음, 만약 댓글에 대한 자세한 정보가 있다면 해당 객체로 변경
    val image_url: String?,  // 이미지 URL은 null일 수 있으므로 nullable로 설정
    val title: String?,     // 제목은 nullable로 설정
    val contents: String?,  // 내용도 nullable
    val location: String?,  // 위치도 nullable
    val view: Int?           // 조회수는 Int로 받음
)

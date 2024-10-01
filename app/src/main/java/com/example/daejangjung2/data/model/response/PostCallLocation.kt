package com.example.daejangjung2.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class PostCallLocationResponse(
    val content: List<PostContent>,  // 게시물 리스트는 PostContent 객체 리스트로 받음
    val currentPage: Int,            // 현재 페이지 번호
    val size: Int,                   // 페이지 사이즈
    val first: Boolean,              // 첫 페이지 여부
    val last: Boolean                // 마지막 페이지 여부
)

@Serializable
data class PostContent(              // 게시물에 대한 데이터를 새로운 객체로 만듦
    val postId: Int,                 // 게시글 ID
    val image_url: String?,          // 이미지 URL
    val title: String?,              // 제목
    val contents: String?,           // 내용
    val location: String?,           // 위치
    val communityComment: List<CommunityComment>,  // 댓글 리스트
    val createdAt: String,           // 생성일
    val updatedAt: String,           // 업데이트일
    val view: Int?,                   // 조회수
    val nickname: String?,
    val profile_image_url: String?
)

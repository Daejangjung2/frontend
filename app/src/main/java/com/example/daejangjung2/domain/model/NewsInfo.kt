package com.example.daejangjung2.domain.model

import com.example.daejangjung2.R

/*
 * TODO 추후에 데이터 변경 예정
 */
data class NewsInfo(
    val id: String,
    val title: String,
    val content: String,
    val imgUrl: Int = R.drawable.sample_image
)

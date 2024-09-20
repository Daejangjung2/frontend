package com.example.daejangjung2.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse (
    val title: String,
    val link: String,
    val imageUrl: String,
    val summary: String,
)
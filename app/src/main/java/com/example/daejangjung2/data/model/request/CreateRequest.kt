package com.example.daejangjung2.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateRequest (
    val title: String,
    val contents: String,
    val location: String,
    val image_url: String,
)
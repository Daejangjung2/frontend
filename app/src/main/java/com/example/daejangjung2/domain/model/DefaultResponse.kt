package com.example.daejangjung2.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DefaultResponse<T:Any>(
    val message: String?,
    val code: Int,
    val data: T,
    val success: Boolean,
)

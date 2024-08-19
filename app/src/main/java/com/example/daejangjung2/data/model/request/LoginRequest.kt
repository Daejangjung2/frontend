package com.example.daejangjung2.data.model.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("id") val id: String,
    @SerialName("pw") val pw: String,
)

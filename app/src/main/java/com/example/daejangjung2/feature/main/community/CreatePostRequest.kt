package com.example.daejangjung2.feature.main.community

data class AuthResponse(
    val message: String,
    val code: Int,
    val data: AuthData,
    val success: Boolean
)


data class AuthData(
    val token: String
)
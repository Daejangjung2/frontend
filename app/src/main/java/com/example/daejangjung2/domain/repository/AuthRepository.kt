package com.example.daejangjung2.domain.repository

import com.example.daejangjung2.data.model.response.Token
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse

interface AuthRepository {
    val isLogin: Boolean
    suspend fun login(id: String, pwd: String): ApiResponse<Token>
    fun getToken(): Token
    suspend fun refreshToken()
    fun removeToken()
    fun updateToken(token: Token)
    suspend fun logout(): ApiResponse<DefaultResponse<Unit>>
}
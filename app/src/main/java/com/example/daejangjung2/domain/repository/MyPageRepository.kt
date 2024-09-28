package com.example.daejangjung2.domain.repository

import com.example.daejangjung2.data.model.response.ProfileResponse
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.model.DefaultResponse

interface MyPageRepository {
    suspend fun profile(): ApiResponse<DefaultResponse<ProfileResponse>>
}
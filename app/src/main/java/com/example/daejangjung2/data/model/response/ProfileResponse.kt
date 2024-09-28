package com.example.daejangjung2.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    @SerialName("Role") val role: String,
    @SerialName("ProfileImg") val profileImg: String,
    @SerialName("Email") val email: String,
    @SerialName("nickname") val nickname: String,
    @SerialName("Gender") val gender: Boolean,
){
    companion object{
        private val EMPTY = ""
        val EMPTY_PROFILE = ProfileResponse(EMPTY,EMPTY,EMPTY,EMPTY,true)
    }
}

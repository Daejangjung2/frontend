package com.example.daejangjung2.feature.main.community

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CommunityPost(
    val title: String,
    val content: String,
    val location: String,
    val imageUrl: String
) : Parcelable

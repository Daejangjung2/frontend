package com.example.daejangjung2.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notice(
    val image: String,
    val comment: String,
): Parcelable {
    companion object{
        private val EMPTY = ""
        val EMPTY_NOTICE = Notice(EMPTY, EMPTY)
    }
}

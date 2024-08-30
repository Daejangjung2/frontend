package com.example.daejangjung2.domain.model

data class Notice(
    val image: String,
    val comment: String,
){
    companion object{
        private val EMPTY = ""
        val EMPTY_NOTICE = Notice(EMPTY, EMPTY)
    }
}

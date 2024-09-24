package com.example.daejangjung2.domain.model

data class Medal(
    val image: String,
    val comment: String,
){
    companion object{
        private val EMPTY = ""
        val EMPTY_MEDAL = Medal(EMPTY, EMPTY)
    }
}

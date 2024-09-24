package com.example.daejangjung2.domain.model

data class ImageBanner(
    val image: String,
){
    companion object{
        const val DEFAULT = ""
        val DEFAULT_BANNER = ImageBanner("")
    }
}

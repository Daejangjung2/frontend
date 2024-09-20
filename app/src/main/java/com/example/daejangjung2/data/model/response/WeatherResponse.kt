package com.example.daejangjung2.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val precipitationProbability: String,
    val humidity: String,
    val precipitationType: String,
    val temperature: String,
    val windSpeed: String,
    val skyCondition: String
){
    companion object{
        const val EMPTY = ""
        val EMPTY_WEATHER = WeatherResponse("","","","","","")
    }
}

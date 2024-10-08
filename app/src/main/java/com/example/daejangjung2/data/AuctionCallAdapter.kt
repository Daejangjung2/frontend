package com.example.daejangjung2.data

import com.example.daejangjung2.domain.model.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class AuctionCallAdapter<T : Any>(private val responseType: Type) :
    CallAdapter<T, Call<ApiResponse<T>>> {
    override fun responseType(): Type = responseType

    override fun adapt(call: Call<T>): Call<ApiResponse<T>> = AuctionCall(call, responseType)
}
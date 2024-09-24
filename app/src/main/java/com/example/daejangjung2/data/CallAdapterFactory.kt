package com.example.daejangjung2.data

import com.example.daejangjung2.domain.model.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class CallAdapterFactory: CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): CallAdapter<*, *>? {
        if (CallAdapter.Factory.getRawType(returnType) != Call::class.java) return null
        check(returnType is ParameterizedType) {
            "Call 반환 타입은 제네릭을 포함해야 합니다."
        }

        val responseType = CallAdapter.Factory.getParameterUpperBound(0, returnType)
        if (CallAdapter.Factory.getRawType(responseType) != ApiResponse::class.java) return null
        check(responseType is ParameterizedType) {
            "Api Response는 제네릭을 포함해야 합니다."
        }

        val genericType = CallAdapter.Factory.getParameterUpperBound(0, responseType)
        return AuctionCallAdapter<Any>(genericType)
    }
}
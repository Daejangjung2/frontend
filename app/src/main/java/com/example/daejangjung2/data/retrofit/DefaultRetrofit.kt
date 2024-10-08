package com.example.daejangjung2.data.retrofit

import android.util.Log
import com.example.daejangjung2.BuildConfig
import com.example.daejangjung2.data.CallAdapterFactory
import com.example.daejangjung2.domain.repository.AuthRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class DefaultRetrofit {

    companion object {
        private const val HTTP_LOG_TAG = "HTTP_LOG"
        private const val AUTHORIZATION = "Authorization"

        fun createInstance(authRepository: AuthRepository): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .client(createOkHttpClient(authRepository))
                .addCallAdapterFactory(CallAdapterFactory())
                .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
                .build()
        }

        private fun createOkHttpClient(authRepository: AuthRepository): OkHttpClient {
            return OkHttpClient.Builder().apply {
                addInterceptor(getHttpLoggingInterceptor())
                addInterceptor(getAuthInterceptor(authRepository))
            }.build()
        }

        private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
            val interceptor = HttpLoggingInterceptor { message ->
                Log.e(HTTP_LOG_TAG, message)
            }
            return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        private fun getAuthInterceptor(authRepository: AuthRepository) =
            Interceptor { chain: Interceptor.Chain ->
                val tokenAddedRequest =
                    chain.request().putTokenHeader(authRepository.getToken().accessToken)

                val response = chain.proceed(tokenAddedRequest)

                if (response.code == 401) {
                    response.close()
                    refreshTokenRequest(authRepository)

                    val refreshedTokenAddedRequest =
                        chain.request().putTokenHeader(authRepository.getToken().accessToken)

                    return@Interceptor chain.proceed(refreshedTokenAddedRequest)
                }

                return@Interceptor response
            }

        private fun refreshTokenRequest(authRepository: AuthRepository) =
            runBlocking {
                authRepository.refreshToken()
            }

        private fun Request.putTokenHeader(token: String): Request {
            Log.d("writepost", "token : ${token}")
            return this.newBuilder()
                .addHeader(AUTHORIZATION, "Bearer "+token)
                .build()
        }
    }
}
package com.example.daejangjung2.data.repository

import android.util.Log
import com.example.daejangjung2.data.datasource.local.LocalAuthDataSource
import com.example.daejangjung2.data.datasource.network.NetworkAuthDataSource
import com.example.daejangjung2.data.model.request.LoginRequest
import com.example.daejangjung2.data.model.response.Token
import com.example.daejangjung2.domain.model.ApiResponse
import com.example.daejangjung2.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class DefaultAuthRepository(
    private val localAuthDataSource: LocalAuthDataSource,
    private val networkAuthDataSource: NetworkAuthDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
): AuthRepository {
    override val isLogin: Boolean
        get() = localAuthDataSource.isLogin();

    override suspend fun login(id: String, pwd: String): ApiResponse<Token> {
        return withContext(dispatcher) {
            val response = networkAuthDataSource.login(
                LoginRequest(
                    id = id,
                    pw = pwd,
                ),
            )


            Log.d(HTTP_LOG_TAG, response.toString())
            if (response is ApiResponse.Success) {
                Log.d(HTTP_LOG_TAG,"Success")
                response.body?.let {
                    localAuthDataSource.updateToken(response.body)
                }
            }

            response
        }
    }

    // 토큰을 내부 저장소에서 가져오는
    override fun getToken(): Token {
        return localAuthDataSource.getToken()
    }

    // 토큰을 갱신하는데 사용
    override fun refreshToken() {
        // TODO
    }

    override fun removeToken() {
        runBlocking {
            localAuthDataSource.removeToken()
        }
    }

    override fun updateToken(token: Token) {
        runBlocking {
            localAuthDataSource.updateToken(token)
        }
    }

    companion object{
        private val HTTP_LOG_TAG = "HTTP_LOG"
    }
}
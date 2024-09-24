package com.example.daejangjung2.data.datasource.local

import android.content.SharedPreferences
import com.example.daejangjung2.data.model.response.Token
import kotlinx.coroutines.runBlocking

class LocalAuthDataSource(private val sharedPref: SharedPreferences) {
    fun getToken(): Token {
        return Token(
            sharedPref.getString("accessToken", "") ?:"",
            sharedPref.getString("refreshToken", "") ?:""
        )
    }

    fun isLogin(): Boolean{
        if(getToken().accessToken != ""){
            return true;
        }
        return false;
    }

    fun updateToken(newToken: Token) {
        with (sharedPref.edit()) {
            putString("accessToken", newToken.accessToken);
            putString("refreshToken", newToken.refreshToken);
            apply()
        }
    }

    fun removeToken() {
        with (sharedPref.edit()) {
            remove("accessToken");
            remove("refreshToken");
            apply()
        }
    }

    companion object {
        const val AUTH_TOKEN_STORE_NAME = "auth_token_data_store.json"
    }
}
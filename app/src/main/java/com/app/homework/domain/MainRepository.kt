package com.app.homework.domain

import com.app.homework.domain.model.LoginRequest
import com.app.homework.domain.model.SignUpRequest
import retrofit2.http.Header

class MainRepository  constructor(private val apiService: ApiService) {

    suspend fun doLogin(loginRequest : LoginRequest) = apiService.doLogin(loginRequest)
    suspend fun doSignUp(signUpRequest : SignUpRequest) = apiService.doSignUp(signUpRequest)
    suspend fun getTransactions(token : String) = apiService.getTransactions(token)

}
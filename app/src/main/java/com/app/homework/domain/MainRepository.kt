package com.app.homework.domain

import com.app.homework.domain.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

class MainRepository  constructor(private val apiService: ApiService) {

    suspend fun doLogin(loginRequest : LoginRequest) = apiService.doLogin(loginRequest)
    suspend fun doSignUp(signUpRequest : SignUpRequest) = apiService.doSignUp(signUpRequest)
    suspend fun getTransactions(token : String) = apiService.getTransactions(token)
    suspend fun getAccountBalance(token : String) = apiService.getAccountBalance(token)
    suspend fun getPayeeList(token : String) = apiService.getPayeeList(token)
    suspend fun doTransfer(token : String,transferRequestModel : TransferRequestModel) = apiService.doTransfer(token,transferRequestModel)
}
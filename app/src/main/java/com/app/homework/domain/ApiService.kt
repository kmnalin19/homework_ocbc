package com.app.homework.domain

import com.app.homework.domain.model.*
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {


    @POST("login")
    suspend fun doLogin(@Body loginRequest : LoginRequest) : Response<LoginResponse>

    @POST("register")
    suspend fun doSignUp(@Body signUpRequest : SignUpRequest) : Response<SignUpResponse>

    @GET("balance")
    suspend fun getAccountBalance(@Header("Authorization") authorization : String) : Response<BalanceResponseModel>

    @GET("transactions")
    suspend fun getTransactions(@Header("Authorization") authorization : String) : Response<TransactionResponse>

    companion object {
        var retrofitService: ApiService? = null
        fun getInstance() : ApiService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://green-thumb-64168.uc.r.appspot.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(ApiService::class.java)
            }
            return retrofitService!!
        }

    }
}
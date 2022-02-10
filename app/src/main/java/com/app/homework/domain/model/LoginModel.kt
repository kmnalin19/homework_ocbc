package com.app.homework.domain.model

data class LoginRequest(val username : String,
                        val password : String)

data class LoginResponse(val status : String,
                         val token : String,
                         val username : String,
                         val accountNo : String)
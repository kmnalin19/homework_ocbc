package com.app.homework.domain.model

import com.app.homework.domain.Response

data class BalanceResponseModel(val status : String,
                                val accountNo : String,
                                val balance : String)
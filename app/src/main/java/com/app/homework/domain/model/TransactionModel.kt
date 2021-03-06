package com.app.homework.domain.model

data class TransactionResponse(val status : String,
                               val data : List<TransactionList>)

data class TransactionList(val amount : Double,
                           val transactionDate : String,
                           val receipient : TransactionRecipient)

data class TransactionRecipient(val accountNo : String = "",
                                val accountHolder : String = "")



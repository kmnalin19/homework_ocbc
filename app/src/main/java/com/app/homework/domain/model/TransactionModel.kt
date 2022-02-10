package com.app.homework.domain.model

data class TransactionResponse(val status : String,
                               val data : List<TransactionList>)

data class TransactionList(val transactionId : String,
                           val amount : Double,
                           val transactionDate : String,
                           val description : String,
                           val transactionType : String,
                           val transactionReceipient : TransactionReceipient)

data class TransactionReceipient(val accountNo : String,
                                 val accountHolder : String)
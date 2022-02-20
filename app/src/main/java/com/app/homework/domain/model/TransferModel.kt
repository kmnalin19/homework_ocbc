package com.app.homework.domain.model

data class TransferRequestModel(val receipientAccountNo : String,
                                val amount : Double,
                                val description : String)




data class TransferResponseModel(val status : String,
                                val transactionId : String,
                                 val amount : Double,
                                 val description : String,
                                 val recipientAccount : String)


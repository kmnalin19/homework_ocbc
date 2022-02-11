package com.app.homework.ui.model

sealed class TransactionRecyclerItem {

    data class TransactionRecyclerTitle(val dateTitle : String) : TransactionRecyclerItem()


    data class TransactionRecyclerRow(val name : String,
                                      val accountNumber : String,
                                      val amount : Double) : TransactionRecyclerItem()
}
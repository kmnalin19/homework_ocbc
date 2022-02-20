package com.app.homework.util

import com.app.homework.domain.model.TransactionList
import com.app.homework.ui.model.TransactionRecyclerItem

object FoundTransferUtil {


    /**
     * prepare TransactionRecyclerItems section list UI model for adapter
     * group from transaction date
     */
    fun getTransactionList(data : List<TransactionList>) : ArrayList<TransactionRecyclerItem>{

        val transactionList : ArrayList<TransactionRecyclerItem> = arrayListOf()
        var currentDate = data[0].transactionDate.split("T")[0]
        var newDate  = currentDate
        transactionList.add(TransactionRecyclerItem.TransactionRecyclerTitle(FormatUtil.getDisplayDateString(currentDate)))
        transactionList.add(TransactionRecyclerItem.TransactionRecyclerRow(
            data[0].receipient.accountHolder,
            data[0].receipient.accountNo,data[0].amount))

        for (i in 2 until data.size){
            newDate = data[i].transactionDate.split("T")[0]
            if (newDate != currentDate) {
                currentDate = newDate
                transactionList.add(
                    TransactionRecyclerItem.TransactionRecyclerTitle(FormatUtil.getDisplayDateString(currentDate)))
            }
            transactionList.add(TransactionRecyclerItem.TransactionRecyclerRow(
                data[i].receipient.accountHolder,
                data[i].receipient.accountNo,data[i].amount))

        }
        return transactionList
    }
}
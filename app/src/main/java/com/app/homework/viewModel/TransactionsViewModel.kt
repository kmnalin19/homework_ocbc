package com.app.homework.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.homework.domain.ApiService
import com.app.homework.domain.MainRepository
import com.app.homework.domain.model.*
import com.app.homework.ui.model.TransactionRecyclerItem
import com.app.homework.util.FormatUtil
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TransactionsViewModel : ViewModel() {

    private var jwtToken : String = ""
    private var accountHolderName : String = ""

    private val apiService: ApiService = ApiService.getInstance()
    private val mainRepository: MainRepository = MainRepository(apiService)

    private val _isTransactionSuccess : MutableLiveData<ArrayList<TransactionRecyclerItem>> = MutableLiveData()
    val isTransactionSuccess : LiveData<ArrayList<TransactionRecyclerItem>>
        get() = _isTransactionSuccess


    private val _isAccountBalanceSuccess : MutableLiveData<BalanceResponseModel> = MutableLiveData()
    val isAccountBalanceSuccess : LiveData<BalanceResponseModel>
        get() = _isAccountBalanceSuccess

    private val _isError : MutableLiveData<String> = MutableLiveData()
    val isError : LiveData<String>
        get() = _isError

    private val _isLoading : MutableLiveData<Boolean> = MutableLiveData()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    fun getTransactions() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getTransactions(jwtToken)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _isTransactionSuccess.postValue(getTransactionList(it.data))
                    }
                    _isLoading.postValue(false)
                } else {
                    _isError.postValue(response.message())
                    _isLoading.postValue(false)
                }
            }
        }
    }

    fun getAccountBalance() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getAccountBalance(jwtToken)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _isAccountBalanceSuccess.postValue(response.body())
                    _isLoading.postValue(false)
                } else {
                    _isError.postValue(response.message())
                    _isLoading.postValue(false)
                }
            }
        }
    }

    private fun getTransactionList(data : List<TransactionList>) : ArrayList<TransactionRecyclerItem>{

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

    private fun onError(message: String) {
        _isError.postValue(message)
    }

    fun setJwtToken(token : String){
        jwtToken = token
    }

    fun setAccountHolderName(name : String){
        accountHolderName = name
    }

    fun getAccountHolderName() = accountHolderName

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        jwtToken = ""
    }

}
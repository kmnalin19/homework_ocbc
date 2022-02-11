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

    /**
     * keep token in view model, better encrypt and keep in local repo and
     */
    private var jwtToken : String = ""
    private var accountHolderName : String = ""

    private val apiService: ApiService = ApiService.getInstance()
    private val mainRepository: MainRepository = MainRepository(apiService)


    /**
     * live data for notify after when Transaction list api success
     */
    private val _isTransactionSuccess : MutableLiveData<ArrayList<TransactionRecyclerItem>> = MutableLiveData()
    val isTransactionSuccess : LiveData<ArrayList<TransactionRecyclerItem>>
        get() = _isTransactionSuccess


    /**
     * live data for notify after when Account details list api success
     */
    private val _isAccountBalanceSuccess : MutableLiveData<BalanceResponseModel> = MutableLiveData()
    val isAccountBalanceSuccess : LiveData<BalanceResponseModel>
        get() = _isAccountBalanceSuccess

    /**
     * live data for notify when Error and handle in UI Fragment or Activity
     */
    private val _isError : MutableLiveData<String> = MutableLiveData()
    val isError : LiveData<String>
        get() = _isError
    /**
     * live data for handle Loading and notify for UI
     */
    private val _isLoading : MutableLiveData<Boolean> = MutableLiveData()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }

    /**
     * call Transaction list api and handle isSuccessful and error (can move to UseCase from viewmodel if want to extend to clean architecture )
     */
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

    /**
     * call account detail api and handle isSuccessful and error (can move to UseCase from viewmodel if want to extend to clean architecture )
     */
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

    /**
     * prepare TransactionRecyclerItems section list UI model for adapter
     * group from transaction date
     */
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

    /**
     * clear job and token
     */
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        jwtToken = ""
    }

}
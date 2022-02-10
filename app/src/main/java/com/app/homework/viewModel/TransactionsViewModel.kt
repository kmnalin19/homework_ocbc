package com.app.homework.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.homework.domain.ApiService
import com.app.homework.domain.MainRepository
import com.app.homework.domain.model.LoginRequest
import com.app.homework.domain.model.LoginResponse
import com.app.homework.domain.model.TransactionResponse
import kotlinx.coroutines.*

class TransactionsViewModel : ViewModel() {

    private var jwtToken : String? = null

    private val apiService: ApiService = ApiService.getInstance()
    private val mainRepository: MainRepository = MainRepository(apiService)

    private val _isTransactionSuccess : MutableLiveData<TransactionResponse> = MutableLiveData()
    val isTransactionSuccess : LiveData<TransactionResponse>
        get() = _isTransactionSuccess

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

    fun getTransactions(token : String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getTransactions(token)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    _isTransactionSuccess.postValue(response.body())
                    _isLoading.postValue(false)
                } else {
                    _isError.postValue(response.message())
                    _isLoading.postValue(false)
                }
            }
        }
    }

    private fun onError(message: String) {
        _isError.postValue(message)
    }

    fun setJwtToken(token : String){
        jwtToken = token
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
        jwtToken = ""
    }

}